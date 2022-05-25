package cn.sunline.saas.schedule.impl

import cn.sunline.saas.formula.*
import cn.sunline.saas.formula.constant.CalculatePrecision
import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.AbstractSchedule
import org.joda.time.Instant
import java.math.BigDecimal
import java.math.RoundingMode

class PayInterestSchedulePrincipalMaturitySchedule(
    amount: BigDecimal,
    interestRateYear: BigDecimal,
    term: LoanTermType,
    frequency: RepaymentFrequency,
    fromDateTime: Instant,
    toDateTime: Instant?,
    baseYearDays: BaseYearDays
) : AbstractSchedule(amount, interestRateYear, term, frequency, fromDateTime, toDateTime) {

    private val baseYearDaysPara = baseYearDays

    override fun getSchedules(): MutableList<Schedule> {

        val periods = CalculatePeriod.calculatePeriods(term, frequency)
        val instalmentAmount = CalculateEqualInstalment.getInstalment(amount, interestRateYear, periods)
        val periodDates = CalculatePeriod.getPeriodDates(fromDateTime, toDateTime, frequency)
        val interestRate = CalculateInterestRate(interestRateYear)

        val schedules = mutableListOf<Schedule>()
        val remainingPrincipal = amount
        for ((index, it) in periodDates.withIndex()) {
            val instalmentPrincipal = if (index == periodDates.size - 1) {
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
            } else {
                remainingPrincipal
            }
            val instalmentInterest = CalculateInterest(amount, interestRate).getDaysInterest(
                fromDateTime,
                toDateTime,
                baseYearDaysPara
            ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            schedules.add(
                Schedule(
                    it.fromDateTime,
                    it.toDateTime,
                    instalmentAmount,
                    instalmentPrincipal,
                    instalmentInterest,
                    remainingPrincipal
                )
            )
        }
        return schedules
    }
}