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

class OneOffRepaymentSchedule(
    amount: BigDecimal,
    interestRateYear: BigDecimal,
    term: LoanTermType,
    frequency: RepaymentFrequency,
    repaymentDayType: RepaymentDayType,
    baseYearDays: BaseYearDays,
    fromDateTime: DateTime,
    toDateTime: DateTime?,
    repaymentDateTime: DateTime?,
    feeAmount: BigDecimal
) : AbstractSchedule(
    amount,
    interestRateYear,
    term,
    frequency,
    repaymentDayType,
    baseYearDays,
    fromDateTime,
    toDateTime,
    repaymentDateTime,
    feeAmount
) {

    override fun getSchedules(): MutableList<Schedule> {

        val interestRate = CalculateInterestRate(interestRateYear)
        val schedules = mutableListOf<Schedule>()
        var fee = feeAmount
        val instalmentPrincipal = amount.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
        val instalmentInterest = CalculateInterest(amount, interestRate).getDaysInterest(
            fromDateTime,
            toDateTime,
            baseYearDays
        ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
        val instalmentAmount = instalmentPrincipal.add(instalmentInterest)
        val period = 1
        schedules.add(
            Schedule(
                fromDateTime,
                toDateTime,
                instalmentAmount.add(fee),
                instalmentPrincipal,
                instalmentInterest,
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                period,
                interestRateYear,
                fee
            )
        )
        return schedules
    }
}