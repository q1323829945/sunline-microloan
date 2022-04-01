

package cn.sunline.saas.repayment.schedule.model.dto

import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.interest.constant.BaseYearDays
import org.joda.time.DateTime
import java.math.BigDecimal


data class DTORepaymentScheduleCalculate  (
    var amount: BigDecimal,
    var term: Int,
    var interestRate: BigDecimal,
    var paymentMethod: PaymentMethodType,
    var startDate: DateTime,
    var endDate: DateTime,
    var repaymentFrequency: RepaymentFrequency? = null,
    var baseYearDays: BaseYearDays = BaseYearDays.ACCOUNT_YEAR,
    var repaymentDay: Int = 21,
    var repaymentDayType: RepaymentDayType = RepaymentDayType.BASE_LOAN_DAY
//
)