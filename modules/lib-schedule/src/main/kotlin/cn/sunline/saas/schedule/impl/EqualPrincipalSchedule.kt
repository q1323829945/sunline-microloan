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
        var instalmentPrincipal = CalculateEqualPrincipal.getPrincipal(amount, periods)
        var firstInterest = BigDecimal.ZERO
        val schedules = mutableListOf<Schedule>()
        var remainingPrincipal = amount
        var period = 0
        var fee = feeAmount
        for ((index, it) in periodDates.withIndex()) {
            val instalmentInterest: BigDecimal
            if (!periodDates[index].isEnough) {
                val principal = if(index == 0) instalmentPrincipal else remainingPrincipal
                instalmentInterest = CalculateInterest(principal, interestRate).getDaysInterest(
                    it.fromDateTime,
                    it.toDateTime,
                    baseYearDays
                ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                firstInterest = instalmentInterest
                if(index == 0) continue
            } else {
                instalmentInterest = CalculateInterest(remainingPrincipal, interestRate).getMonthInterest(
                    frequency.term.toMonthUnit().num
                ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP).add(firstInterest)
            }

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
                    instalmentAmount.add(fee),
                    instalmentPrincipal,
                    instalmentInterest,
                    remainingPrincipal,
                    period,
                    interestRateYear,
                    fee
                )
            )
            firstInterest = BigDecimal.ZERO
            fee = BigDecimal.ZERO
        }
        return schedules
    }
}