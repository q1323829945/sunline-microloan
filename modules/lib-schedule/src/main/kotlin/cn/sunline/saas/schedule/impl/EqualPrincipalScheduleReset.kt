package cn.sunline.saas.schedule.impl

import cn.sunline.saas.formula.CalculateEqualPrincipal
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


/**
 * @tittle :
 * @description :
 * @author : xujm
 * @date : 2022/6/16 17:00
 */
class EqualPrincipalScheduleReset(
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
        var instalmentPrincipal = CalculateEqualPrincipal.getPrincipal(amount, periodDates.size)

        for ((index, it) in periodDates.withIndex()) {
            val instalmentInterest = CalculateInterest(remainingPrincipal, interestRate).getMonthInterest(
                frequency.term.toMonthUnit().num
            ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            remainingPrincipal = remainingPrincipal.subtract(instalmentPrincipal)

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
                    interestRateYear,
                    feeAmount
                )
            )
        }
        return schedules
    }

}