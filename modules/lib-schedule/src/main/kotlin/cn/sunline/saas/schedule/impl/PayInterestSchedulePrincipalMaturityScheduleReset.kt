package cn.sunline.saas.schedule.impl

import cn.sunline.saas.formula.CalculateInterest
import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.formula.CalculatePeriod
import cn.sunline.saas.formula.constant.CalculatePrecision
import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.AbstractSchedule
import cn.sunline.saas.schedule.Schedule
import org.joda.time.DateTime
import java.math.BigDecimal
import java.math.RoundingMode


class PayInterestSchedulePrincipalMaturityScheduleReset(
    remainAmount: BigDecimal,
    interestRateYear: BigDecimal,
    term: LoanTermType,
    frequency: RepaymentFrequency,
    repaymentDayType: RepaymentDayType?,
    baseYearDays: BaseYearDays,
    fromDateTime: DateTime,
    toDateTime: DateTime?,
    repaymentDateTime: DateTime?,
    feeAmount: BigDecimal
) : AbstractSchedule(
    remainAmount,
    interestRateYear,
    term,
    frequency,
    repaymentDayType,
    baseYearDays,
    fromDateTime,
    toDateTime,
    repaymentDateTime,
    feeAmount
)  {

    override fun getSchedules(): MutableList<Schedule> {

        var remainingPrincipal = amount
        val interestRate = CalculateInterestRate(interestRateYear)
        val schedules = mutableListOf<Schedule>()
        var period = 0
        if (repaymentDateTime!! != fromDateTime) {
            val advanceInterest = CalculateInterest(amount, interestRate)
                .getDaysInterest(repaymentDateTime, fromDateTime, baseYearDays)
                .setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)

            period++

            schedules.add(
                Schedule(
                    repaymentDateTime,
                    fromDateTime,
                    advanceInterest,
                    BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                    advanceInterest,
                    remainingPrincipal,
                    period,
                    interestRateYear,
                    feeAmount
                )
            )

        }

        val periodDates = CalculatePeriod.getPeriodDates(fromDateTime, toDateTime, frequency)
        for ((index, it) in periodDates.withIndex()) {
            val instalmentPrincipal: BigDecimal
            if (index == periodDates.size - 1) {
                remainingPrincipal = BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                instalmentPrincipal = amount.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            } else {
                remainingPrincipal = amount.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                instalmentPrincipal = BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            }
            val instalmentInterest = CalculateInterest(amount, interestRate).getDaysInterest(
                it.fromDateTime,
                it.toDateTime,
                baseYearDays
            ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)

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
                    interestRateYear,
                    feeAmount
                )
            )
        }
        return schedules
    }
}