

package cn.sunline.saas.repayment.schedule.model.dto

import cn.sunline.saas.global.constant.*
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import org.joda.time.Instant
import java.math.BigDecimal
import java.util.*


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
)


data class DTORepaymentScheduleResetCalculate (
    var remainLoanAmount: BigDecimal,
    var repaymentDate: Instant,
    var repaymentDay: Int,
    var baseYearDays: BaseYearDays,
    var paymentMethod: PaymentMethodType,
    var repaymentFrequency: RepaymentFrequency,
    var oldRepaymentSchedule: RepaymentSchedule
)

data class DTORepaymentScheduleResetResult (
    var currentPeriod:Int,
    var nextPeriod:Int,
    var remainPeriods:Int,
    var nextRepaymentDate: Date,
)