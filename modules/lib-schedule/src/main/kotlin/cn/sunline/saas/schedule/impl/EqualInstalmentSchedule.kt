package cn.sunline.saas.schedule.impl

import cn.sunline.saas.formula.*
import cn.sunline.saas.formula.constant.CalculatePrecision
import cn.sunline.saas.global.constant.LoanTermType
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
    fromDateTime: DateTime,
    toDateTime: DateTime?
) : AbstractSchedule(amount, interestRateYear, term, frequency, fromDateTime, toDateTime) {

    override fun getSchedules(): MutableList<Schedule> {
        val periods = CalculatePeriod.calculatePeriods(term, frequency)
        val instalmentAmount = CalculateEqualInstalment.getInstalment(amount, interestRateYear, periods)
        val periodDates = CalculatePeriod.getPeriodDates(fromDateTime, toDateTime, frequency)
        val interestRate = CalculateInterestRate(interestRateYear)

        val schedules = mutableListOf<Schedule>()
        var remainingPrincipal = amount
        var period = 0
        for ((index, it) in periodDates.withIndex()) {
            val instalmentPrincipal: BigDecimal
            val instalmentInterest: BigDecimal
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
            remainingPrincipal = remainingPrincipal.subtract(instalmentPrincipal)
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