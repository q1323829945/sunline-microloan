package cn.sunline.saas.schedule.impl

import cn.sunline.saas.formula.*
import cn.sunline.saas.formula.constant.CalculatePrecision
import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.AbstractSchedule
import cn.sunline.saas.schedule.Schedule
import org.joda.time.DateTime
import org.joda.time.Instant
import java.math.BigDecimal
import java.math.RoundingMode


class EqualPrincipalSchedule(
    amount: BigDecimal,
    interestRateYear: BigDecimal,
    term: LoanTermType,
    frequency: RepaymentFrequency,
    repaymentDayType: RepaymentDayType,
    baseYearDays: BaseYearDays,
    fromDateTime: DateTime,
    toDateTime: DateTime?,
    repaymentDateTime: DateTime?
) : AbstractSchedule(
    amount,
    interestRateYear,
    term,
    frequency,
    repaymentDayType,
    baseYearDays,
    fromDateTime,
    toDateTime,
    repaymentDateTime
)  {
    override fun getSchedules(): MutableList<Schedule> {

        var periods = CalculatePeriod.calculatePeriods(term, frequency)
        val periodDates = CalculatePeriod.getPeriodDates(fromDateTime, toDateTime, frequency)
        val interestRate = CalculateInterestRate(interestRateYear)
        var instalmentPrincipal = CalculateEqualPrincipal.getPrincipal(amount, periods)

        val schedules = mutableListOf<Schedule>()
        var remainingPrincipal = amount
        var period = 0
        for ((index, it) in periodDates.withIndex()) {
            val instalmentInterest: BigDecimal
            if (!periodDates[index].isEnough) {
                instalmentPrincipal = BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                instalmentInterest = CalculateInterest(remainingPrincipal, interestRate).getDaysInterest(
                    fromDateTime,
                    toDateTime,
                    baseYearDays
                ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            } else {
                instalmentInterest = CalculateInterest(remainingPrincipal, interestRate).getMonthInterest(
                    frequency.term.toMonthUnit().num
                ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                remainingPrincipal = remainingPrincipal.subtract(instalmentPrincipal)
            }

            if (index == periodDates.size - 1 && instalmentPrincipal != BigDecimal.ZERO) {
                instalmentPrincipal = instalmentPrincipal.add(remainingPrincipal)
                remainingPrincipal = BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            }

            val instalmentAmount = instalmentPrincipal.add(instalmentInterest)
            period++

            schedules.add(
                Schedule(
                    it.fromDateTime,
                    it.toDateTime,
                    instalmentAmount,
                    instalmentPrincipal,
                    instalmentInterest,
                    remainingPrincipal,
                    period,
                    interestRateYear
                )
            )
        }
        return schedules
    }
}