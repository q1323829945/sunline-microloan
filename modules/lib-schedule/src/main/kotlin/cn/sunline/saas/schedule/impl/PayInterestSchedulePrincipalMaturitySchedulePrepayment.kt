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

class PayInterestSchedulePrincipalMaturitySchedulePrepayment(
    amount: BigDecimal,
    interestRateYear: BigDecimal,
    term: LoanTermType,
    frequency: RepaymentFrequency,
    repaymentDayType: RepaymentDayType?,
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

        var fee = feeAmount
        val oldSchedules = PayInterestSchedulePrincipalMaturitySchedule(
            amount,
            interestRateYear,
            term,
            frequency,
            repaymentDayType!!,
            baseYearDays,
            fromDateTime,
            toDateTime,
            repaymentDateTime,
            fee
        ).getSchedules()

        val interestRate = CalculateInterestRate(interestRateYear)
        var calcInterestFlag = true
        val newSchedules = mutableListOf<Schedule>()
        oldSchedules.forEach {
            if (repaymentDateTime!! < it.dueDate) {
                val instalmentInterest =
                    if (calcInterestFlag) {
                        calcInterestFlag = false
                        CalculateInterest(amount, interestRate).getDaysInterest(
                            fromDateTime,
                            repaymentDateTime,
                            baseYearDays
                        ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                    } else {
                        BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
                    }
                newSchedules.add(
                    Schedule(
                        it.fromDate,
                        it.dueDate,
                        it.principal.add(instalmentInterest).add(fee),
                        it.principal,
                        instalmentInterest,
                        it.remainingPrincipal,
                        it.period,
                        it.interestRate,
                        fee
                    )
                )
                fee = BigDecimal.ZERO
            }
        }
        return newSchedules
    }
}