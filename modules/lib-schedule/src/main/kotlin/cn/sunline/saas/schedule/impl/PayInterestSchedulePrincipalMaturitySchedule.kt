package cn.sunline.saas.schedule.impl

import cn.sunline.saas.formula.*
import cn.sunline.saas.global.constant.CalculatePrecision
import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.AbstractSchedule
import cn.sunline.saas.schedule.Schedule
import org.joda.time.DateTime
import java.math.BigDecimal
import java.math.RoundingMode

class PayInterestSchedulePrincipalMaturitySchedule(
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
        val periods = CalculatePeriod.calculatePeriods(term, frequency)
        val periodDates = CalculatePeriod.getPeriodDates(fromDateTime, toDateTime, frequency, repaymentDayType!!)
        val interestRate = CalculateInterestRate(interestRateYear)
        val firstInterest = CalculateInterest(amount, interestRate).getFirstInterest(periods, periodDates, baseYearDays)
        if (periods != periodDates.size) {
            periodDates.removeFirst()
        }
        var fee = feeAmount
        val schedules = mutableListOf<Schedule>()
        var period = 0
        for ((index, it) in periodDates.withIndex()) {
            val remainingPrincipal: BigDecimal
            val instalmentPrincipal: BigDecimal
            if (index == periodDates.size - 1) {
                remainingPrincipal = BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                instalmentPrincipal = amount.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            } else {
                remainingPrincipal = amount.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                instalmentPrincipal = BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            }
            var instalmentInterest = CalculateInterest(amount, interestRate).getDaysInterest(
                it.fromDateTime,
                it.toDateTime,
                baseYearDays
            ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            instalmentInterest = if(index == 0) instalmentInterest.add(firstInterest) else instalmentInterest

            val instalmentAmount = instalmentPrincipal.add(instalmentInterest)
            period++
            schedules.add(
                Schedule(
                    if(index == 0) fromDateTime else it.fromDateTime,
                    it.toDateTime,
                    instalmentAmount.add(fee),
                    instalmentPrincipal,
                    instalmentInterest,
                    remainingPrincipal,
                    period,
                    interestRateYear,
                    fee
                )
            )
            fee = BigDecimal.ZERO
        }
        return schedules
    }
}