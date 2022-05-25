package cn.sunline.saas.schedule.impl

import cn.sunline.saas.formula.*
import cn.sunline.saas.formula.constant.CalculatePrecision
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.AbstractSchedule
import org.joda.time.Instant
import java.math.BigDecimal
import java.math.RoundingMode


class EqualPrincipalSchedule(
    amount: BigDecimal,
    interestRateYear: BigDecimal,
    term: LoanTermType,
    frequency: RepaymentFrequency,
    fromDateTime: Instant,
    toDateTime: Instant?
) : AbstractSchedule(amount, interestRateYear, term, frequency, fromDateTime, toDateTime) {
    override fun getSchedules(): MutableList<Schedule> {

        val periods = CalculatePeriod.calculatePeriods(term, frequency)
        val periodDates = CalculatePeriod.getPeriodDates(fromDateTime, toDateTime, frequency)
        val interestRate = CalculateInterestRate(interestRateYear)
        val instalmentPrincipal = CalculateEqualPrincipal.getPrincipal(amount,periods)

        val schedules = mutableListOf<Schedule>()
        var remainingPrincipal = amount
        for ((index, it) in periodDates.withIndex()) {

            val instalmentInterest = if (index == periodDates.size - 1) {
                remainingPrincipal.subtract(instalmentPrincipal)
            } else {
                CalculateInterest(remainingPrincipal, interestRate).getMonthInterest(
                    frequency.term.toMonthUnit().num
                ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            }

            remainingPrincipal = remainingPrincipal.subtract(instalmentPrincipal)
            schedules.add(Schedule(it.fromDateTime,it.toDateTime, null, instalmentPrincipal, instalmentInterest, remainingPrincipal))
        }
        return schedules
    }
}