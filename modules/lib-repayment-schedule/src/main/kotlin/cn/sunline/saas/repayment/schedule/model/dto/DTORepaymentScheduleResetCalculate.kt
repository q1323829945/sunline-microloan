package cn.sunline.saas.repayment.schedule.model.dto

import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.interest.constant.BaseYearDays
import org.joda.time.DateTime
import java.math.BigDecimal


data class DTORepaymentScheduleResetCalculate (
//    var loanId: Long,
    var repaymentScheduleId: Long,
    var remainLoanAmount: BigDecimal,
    var loanRate: BigDecimal,
    var repaymentDate: DateTime,
    var repaymentDay: Int = 0,
    var baseYearDays: BaseYearDays = BaseYearDays.ACCOUNT_YEAR,
    var paymentMethod: PaymentMethodType,
//    var repaymentFrequency: RepaymentFrequency = RepaymentFrequency.ONE_MONTH,
)