package cn.sunline.saas.schedule.impl

import cn.sunline.saas.formula.*
import cn.sunline.saas.formula.constant.CalculatePrecision
import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.AbstractSchedule
import cn.sunline.saas.schedule.Schedule
import org.joda.time.Instant
import java.math.BigDecimal
import java.math.RoundingMode

class PayInterestSchedulePrincipalMaturitySchedule(
    amount: BigDecimal,
    interestRateYear: BigDecimal,
    term: LoanTermType,
    frequency: RepaymentFrequency,
    fromDateTime: Instant,
    toDateTime: Instant?,
    baseYearDays: BaseYearDays
) : AbstractSchedule(amount, interestRateYear, term, frequency, fromDateTime, toDateTime) {

    private val baseYearDaysPara = baseYearDays

    override fun getSchedules(): MutableList<Schedule> {

        val periodDates = CalculatePeriod.getPeriodDates(fromDateTime, toDateTime, frequency)
        val interestRate = CalculateInterestRate(interestRateYear)

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
            val instalmentInterest = CalculateInterest(amount, interestRate).getDaysInterest(
                fromDateTime,
                toDateTime,
                baseYearDaysPara
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
                    interestRateYear
                )
            )
        }
        return schedules
    }
}