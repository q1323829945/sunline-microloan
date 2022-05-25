package cn.sunline.saas.schedule

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.PaymentMethodType.*
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.impl.EqualInstalmentSchedule
import org.joda.time.Instant
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
    private val fromDateTime: Instant,
    private val toDateTime: Instant?,
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
            EQUAL_PRINCIPAL -> TODO()
            ONE_OFF_REPAYMENT -> TODO()
            PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY -> TODO()
        }

        return scheduleMethod.getSchedules()
    }
}