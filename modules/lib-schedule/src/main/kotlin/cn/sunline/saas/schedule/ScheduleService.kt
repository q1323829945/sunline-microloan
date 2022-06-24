package cn.sunline.saas.schedule

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.PaymentMethodType.*
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.impl.*

import org.joda.time.DateTime
import java.math.BigDecimal

/**
 * @title: ScheduleService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/20 15:16
 */
class ScheduleService(
    private val amount: BigDecimal,
    private val interestRateYear: BigDecimal,
    private val term: LoanTermType,
    private val frequency: RepaymentFrequency,
    private val fromDateTime: DateTime,
    private val toDateTime: DateTime?,
    private val baseYearDays: BaseYearDays,
    private val repaymentDateTime: DateTime?
) {
    fun getSchedules(paymentMethodType: PaymentMethodType): MutableList<Schedule> {
        val scheduleMethod = when (paymentMethodType) {
            EQUAL_INSTALLMENT -> EqualInstalmentSchedule(
                amount,
                interestRateYear,
                term,
                frequency,
                fromDateTime,
                toDateTime
            )
            EQUAL_PRINCIPAL -> EqualPrincipalSchedule(
                amount,
                interestRateYear,
                term,
                frequency,
                fromDateTime,
                toDateTime
            )
            ONE_OFF_REPAYMENT -> OneOffRepaymentSchedule(
                amount,
                interestRateYear,
                term,
                frequency,
                fromDateTime,
                toDateTime,
                baseYearDays
            )
            PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY -> PayInterestSchedulePrincipalMaturitySchedule(
                amount,
                interestRateYear,
                term,
                frequency,
                fromDateTime,
                toDateTime,
                baseYearDays
            )
        }
        return scheduleMethod.getSchedules()
    }

    fun getResetSchedules(paymentMethodType: PaymentMethodType): MutableList<Schedule> {
        val scheduleMethod = when (paymentMethodType) {
            EQUAL_INSTALLMENT -> EqualInstalmentScheduleReset(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDateTime!!,
                fromDateTime,
                toDateTime!!,
                baseYearDays
            )
            EQUAL_PRINCIPAL -> EqualPrincipalScheduleReset(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDateTime!!,
                fromDateTime,
                toDateTime!!,
                baseYearDays
            )
            ONE_OFF_REPAYMENT -> OneOffRepaymentScheduleReset(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDateTime!!,
                fromDateTime,
                toDateTime!!,
                baseYearDays
            )
            PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY -> PayInterestSchedulePrincipalMaturityScheduleReset(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDateTime!!,
                fromDateTime,
                toDateTime!!,
                baseYearDays
            )
        }
        return scheduleMethod.getSchedules()
    }
}