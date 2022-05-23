

package cn.sunline.saas.repayment.schedule.model.dto

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import org.joda.time.Instant
import java.math.BigDecimal


data class DTORepaymentScheduleResetCalculate (
    var remainLoanAmount: BigDecimal,
    var repaymentDate: Instant,
    var repaymentDay: Int,
    var baseYearDays: BaseYearDays,
    var paymentMethod: PaymentMethodType,
    var repaymentFrequency: RepaymentFrequency,
    var oldRepaymentSchedule: RepaymentSchedule
)