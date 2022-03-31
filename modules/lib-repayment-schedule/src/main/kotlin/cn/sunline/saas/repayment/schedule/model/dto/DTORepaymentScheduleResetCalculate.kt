

package cn.sunline.saas.repayment.schedule.model.dto

import cn.sunline.saas.interest.model.BaseYearDays
import cn.sunline.saas.repayment.model.PaymentMethodType
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