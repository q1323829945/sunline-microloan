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

class PayInterestSchedulePrincipalMaturitySchedulePrepayment (
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
)  {

    override fun getSchedules(): MutableList<Schedule> {

        var remainingPrincipal = amount
        val interestRate = CalculateInterestRate(interestRateYear)
        val schedules = mutableListOf<Schedule>()
        var period = 0

        val periodDates = CalculatePeriod.getPeriodDates(fromDateTime, toDateTime, frequency)
        for ((index, it) in periodDates.withIndex()) {
            val instalmentPrincipal: BigDecimal
            val instalmentInterest =
                if (period == 0)
                    CalculateInterest(amount, interestRate).getDaysInterest(
                        fromDateTime,
                        repaymentDateTime!!,
                        baseYearDays
                    ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                else
                    BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)


            if (index == periodDates.size - 1) {
                remainingPrincipal = BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                instalmentPrincipal = amount.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            } else {
                remainingPrincipal = amount.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                instalmentPrincipal = BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
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