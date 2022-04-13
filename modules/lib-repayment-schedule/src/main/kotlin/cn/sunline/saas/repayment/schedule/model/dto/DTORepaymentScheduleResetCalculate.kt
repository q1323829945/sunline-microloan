

package cn.sunline.saas.repayment.schedule.model.dto

import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.interest.constant.BaseYearDays
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import org.joda.time.DateTime
import java.math.BigDecimal


data class DTORepaymentScheduleResetCalculate (
    var remainLoanAmount: BigDecimal,
    var repaymentDate: String,
    var repaymentDay: Int,
    var baseYearDays: BaseYearDays,
    var paymentMethod: PaymentMethodType,
    var repaymentFrequency: RepaymentFrequency,
    var oldRepaymentSchedule: RepaymentSchedule
)