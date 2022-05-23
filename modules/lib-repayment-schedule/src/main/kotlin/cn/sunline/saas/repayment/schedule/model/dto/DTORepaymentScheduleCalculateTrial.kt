

package cn.sunline.saas.repayment.schedule.model.dto

import cn.sunline.saas.global.constant.*
import org.joda.time.Instant
import java.math.BigDecimal


data class DTORepaymentScheduleCalculateTrial  (
    var amount: BigDecimal,
    var term: LoanTermType,
    var interestRate: BigDecimal,
    var paymentMethod: PaymentMethodType,
    var startDate: Instant,
    var endDate: Instant,
    var repaymentFrequency: RepaymentFrequency = RepaymentFrequency.ONE_MONTH,
    var baseYearDays: BaseYearDays = BaseYearDays.ACCOUNT_YEAR,
    var repaymentDay: Int = 21,
    var repaymentDayType: RepaymentDayType = RepaymentDayType.BASE_LOAN_DAY
//
)