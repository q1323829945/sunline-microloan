package cn.sunline.saas.schedule

import cn.sunline.saas.global.constant.*
import cn.sunline.saas.global.constant.PaymentMethodType.*
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
    private val repaymentDayType: RepaymentDayType?,
    private val baseYearDays: BaseYearDays,
    private val fromDateTime: DateTime,
    private val toDateTime: DateTime?,
    private val repaymentDateTime: DateTime?
) {
    fun getSchedules(paymentMethodType: PaymentMethodType): MutableList<Schedule> {
        val scheduleMethod = when (paymentMethodType) {
            EQUAL_INSTALLMENT -> EqualInstalmentSchedule(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDayType!!,
                baseYearDays,
                fromDateTime,
                toDateTime,
                repaymentDateTime
            )
            EQUAL_PRINCIPAL -> EqualPrincipalSchedule(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDayType!!,
                baseYearDays,
                fromDateTime,
                toDateTime,
                repaymentDateTime
            )
            ONE_OFF_REPAYMENT -> OneOffRepaymentSchedule(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDayType!!,
                baseYearDays,
                fromDateTime,
                toDateTime,
                repaymentDateTime
            )
            PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY -> PayInterestSchedulePrincipalMaturitySchedule(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDayType!!,
                baseYearDays,
                fromDateTime,
                toDateTime,
                repaymentDateTime
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
                repaymentDayType,
                baseYearDays,
                fromDateTime,
                toDateTime!!,
                repaymentDateTime!!,
            )
            EQUAL_PRINCIPAL -> EqualPrincipalScheduleReset(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDayType,
                baseYearDays,
                fromDateTime,
                toDateTime!!,
                repaymentDateTime!!,
            )
            ONE_OFF_REPAYMENT -> OneOffRepaymentScheduleReset(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDayType,
                baseYearDays,
                fromDateTime,
                toDateTime!!,
                repaymentDateTime!!,
            )
            PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY -> PayInterestSchedulePrincipalMaturityScheduleReset(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDayType,
                baseYearDays,
                fromDateTime,
                toDateTime!!,
                repaymentDateTime!!,
            )
        }
        return scheduleMethod.getSchedules()
    }

    fun getPrepaymentSchedules(paymentMethodType: PaymentMethodType): MutableList<Schedule>{
        val scheduleMethod = when (paymentMethodType) {
            EQUAL_INSTALLMENT -> EqualInstalmentSchedulePrepayment(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDayType,
                baseYearDays,
                fromDateTime,
                toDateTime!!,
                repaymentDateTime!!,
            )
            EQUAL_PRINCIPAL -> EqualPrincipalScheduleReset(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDayType,
                baseYearDays,
                fromDateTime,
                toDateTime!!,
                repaymentDateTime!!,
            )
            ONE_OFF_REPAYMENT -> OneOffRepaymentScheduleReset(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDayType,
                baseYearDays,
                fromDateTime,
                toDateTime!!,
                repaymentDateTime!!,
            )
            PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY -> PayInterestSchedulePrincipalMaturityScheduleReset(
                amount,
                interestRateYear,
                term,
                frequency,
                repaymentDayType,
                baseYearDays,
                fromDateTime,
                toDateTime!!,
                repaymentDateTime!!,
            )
        }
        return scheduleMethod.getSchedules()
    }
}