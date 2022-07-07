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
    repaymentDayType: RepaymentDayType = RepaymentDayType.BASE_LOAN_DAY,
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
        val periods = CalculatePeriod.calculatePeriods(term, frequency)
        val periodDates = CalculatePeriod.getPeriodDates(fromDateTime, toDateTime, frequency, repaymentDayType!!)
        val interestRate = CalculateInterestRate(interestRateYear)
        val instalmentAmount = CalculateEqualInstalment.getInstalment(amount, interestRateYear, periods)
        val schedules = mutableListOf<Schedule>()
        var remainingPrincipal = amount
        var period = 0
        var firstInterest = BigDecimal.ZERO
        var firstPrincipal = BigDecimal.ZERO
        for ((index, it) in periodDates.withIndex()) {
            var instalmentPrincipal : BigDecimal
            var instalmentInterest: BigDecimal
            if (index == periodDates.size - 1) {
                instalmentPrincipal =  remainingPrincipal.add(firstPrincipal)
                instalmentInterest = instalmentAmount.subtract(instalmentPrincipal)
            } else {
                instalmentInterest = if(!it.isEnough){
                    CalculateInterest(remainingPrincipal, interestRate).getDaysInterest(
                        it.fromDateTime,
                        it.toDateTime,
                        baseYearDays
                    ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                }else {
                    CalculateInterest(remainingPrincipal, interestRate).getMonthInterest(
                        frequency.term.toMonthUnit().num
                    ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                }
            }
            instalmentPrincipal = instalmentAmount.subtract(instalmentInterest)
            if(index == 0){
                firstPrincipal = instalmentPrincipal
                firstInterest = instalmentInterest
                remainingPrincipal = remainingPrincipal.subtract(instalmentPrincipal)
                continue
            }
            if (index == 1 && firstInterest != BigDecimal.ZERO){
                instalmentInterest =  instalmentInterest.add(firstInterest)
                instalmentPrincipal = instalmentPrincipal.subtract(firstInterest)
            }
            remainingPrincipal = if(remainingPrincipal <= BigDecimal.ZERO) BigDecimal.ZERO else remainingPrincipal.subtract(instalmentPrincipal)

            period++

            schedules.add(
                Schedule(
                    if(index == 1 && firstInterest != BigDecimal.ZERO) fromDateTime else it.fromDateTime,
                    it.toDateTime,
                    instalment = instalmentAmount,
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