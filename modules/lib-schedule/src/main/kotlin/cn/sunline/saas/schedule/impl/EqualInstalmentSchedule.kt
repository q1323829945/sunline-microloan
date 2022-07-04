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
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @title: CalculateEqualInstalment
 * @description:
 * @author Kevin-Cui
 * @date 2022/5/19 10:10
 */
class EqualInstalmentSchedule(
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
) {

    override fun getSchedules(): MutableList<Schedule> {
        var periods = CalculatePeriod.calculatePeriods(term, frequency)
        val periodDates = CalculatePeriod.getPeriodDates(fromDateTime, toDateTime, frequency)
//        if ((repaymentDayType == RepaymentDayType.MONTH_FIRST_DAY && fromDateTime.dayOfMonth != fromDateTime.dayOfMonth()
//                .withMinimumValue().dayOfMonth) ||
//            (repaymentDayType == RepaymentDayType.MONTH_LAST_DAY && fromDateTime.dayOfMonth != fromDateTime.dayOfMonth()
//                .withMaximumValue().dayOfMonth)
//        ) {
//            periods -= 2
//        }
        val interestRate = CalculateInterestRate(interestRateYear)
        val instalmentAmount = CalculateEqualInstalment.getInstalment(amount, interestRateYear, periods)
        val schedules = mutableListOf<Schedule>()
        var remainingPrincipal = amount
        var period = 0
        for ((index, it) in periodDates.withIndex()) {
            val instalmentPrincipal: BigDecimal
            val instalmentInterest: BigDecimal
            if (!periodDates[index].isEnough) {
                instalmentPrincipal = BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                instalmentInterest = CalculateInterest(remainingPrincipal, interestRate).getDaysInterest(
                    fromDateTime,
                    toDateTime,
                    baseYearDays
                ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            } else {
                if (index == periodDates.size - 1) {
                    instalmentPrincipal = remainingPrincipal
                    instalmentInterest = instalmentAmount.subtract(instalmentPrincipal)
                } else {
                    instalmentInterest =
                        CalculateInterest(remainingPrincipal, interestRate).getMonthInterest(
                            frequency.term.toMonthUnit().num
                        ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)

                    instalmentPrincipal = instalmentAmount.subtract(instalmentInterest)
                }
            }
            remainingPrincipal = remainingPrincipal.subtract(instalmentPrincipal)
            period++

            schedules.add(
                Schedule(
                    it.fromDateTime,
                    it.toDateTime,
                    instalment = if(!periodDates[index].isEnough) instalmentPrincipal.add(instalmentInterest) else instalmentAmount,
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