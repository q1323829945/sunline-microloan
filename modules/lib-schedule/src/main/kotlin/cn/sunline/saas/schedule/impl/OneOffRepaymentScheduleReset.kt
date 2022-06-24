package cn.sunline.saas.schedule.impl

import cn.sunline.saas.formula.CalculateInterest
import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.formula.constant.CalculatePrecision
import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.AbstractSchedule
import cn.sunline.saas.schedule.Schedule
import org.joda.time.DateTime
import java.math.BigDecimal
import java.math.RoundingMode


class OneOffRepaymentScheduleReset (
    remainAmount: BigDecimal,
    interestRateYear: BigDecimal,
    term: LoanTermType,
    frequency: RepaymentFrequency,
    repaymentDate: DateTime,
    fromDateTime: DateTime,
    toDateTime: DateTime,
    baseYearDays: BaseYearDays
) : AbstractSchedule(remainAmount, interestRateYear, term, frequency, fromDateTime.toDateTime(), toDateTime.toDateTime()) {

    private val baseYearDaysPara = baseYearDays

    private val repaymentDatePara = repaymentDate

    override fun getSchedules(): MutableList<Schedule> {

        val remainingPrincipal = amount
        val interestRate = CalculateInterestRate(interestRateYear)
        val schedules = mutableListOf<Schedule>()
        var period = 0
        if (repaymentDatePara != fromDateTime) {
            val advanceInterest = CalculateInterest(amount, interestRate)
                .getDaysInterest(repaymentDatePara, fromDateTime, baseYearDaysPara)
                .setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)

            period++

            schedules.add(
                Schedule(
                    repaymentDatePara,
                    fromDateTime,
                    advanceInterest,
                    BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                    advanceInterest,
                    remainingPrincipal,
                    period,
                    interestRateYear
                )
            )

        }

        val instalmentPrincipal = amount.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
        val instalmentInterest = CalculateInterest(amount, interestRate).getDaysInterest(
            fromDateTime,
            toDateTime,
            baseYearDaysPara).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
        val instalmentAmount = instalmentPrincipal.add(instalmentInterest)
        schedules.add(
            Schedule(
                fromDateTime,
                toDateTime,
                instalmentAmount,
                instalmentPrincipal,
                instalmentInterest,
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                period++,
                interestRateYear
            )
        )

        return schedules
    }
}