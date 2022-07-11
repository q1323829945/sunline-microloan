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


class EqualInstalmentSchedulePrepayment(
    remainAmount: BigDecimal,
    interestRateYear: BigDecimal,
    term: LoanTermType,
    frequency: RepaymentFrequency,
    repaymentDayType: RepaymentDayType?,
    baseYearDays: BaseYearDays,
    fromDateTime: DateTime,
    toDateTime: DateTime?,
    repaymentDateTime: DateTime?
) : AbstractSchedule(
    remainAmount,
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

        var remainingPrincipal = amount
        val interestRate = CalculateInterestRate(interestRateYear)
        val schedules = mutableListOf<Schedule>()
        var period = 0
        val periodDates = CalculatePeriod.getPeriodDates(fromDateTime, toDateTime, frequency)
        var instalmentPrincipal = CalculateEqualPrincipal.getPrincipal(amount, periodDates.size)
        for ((index, it) in periodDates.withIndex()) {

            val instalmentInterest =
                if (period == 0)
                    CalculateInterest(amount, interestRate).getDaysInterest(
                        fromDateTime,
                        repaymentDateTime!!,
                        baseYearDays
                    ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                else
                    BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)

            remainingPrincipal = remainingPrincipal.subtract(instalmentPrincipal)
            if (index == periodDates.size - 1 && instalmentPrincipal != BigDecimal.ZERO) {
                instalmentPrincipal = instalmentPrincipal.add(remainingPrincipal)
                remainingPrincipal = BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            }
            period++

            val instalmentAmount = instalmentPrincipal.add(instalmentInterest)
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
