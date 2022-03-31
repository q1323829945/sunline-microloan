package cn.sunline.saas.repayment.schedule.model.dto

import cn.sunline.saas.repayment.model.PaymentMethodType
import cn.sunline.saas.repayment.model.RepaymentFrequency
import java.math.BigDecimal


data class DTOPrincipalCalculator (
    var calcAmount: BigDecimal,
    var paymentMethod : PaymentMethodType,
    var interestRate: BigDecimal,
    var periods: Int,
    var period: Int,
    var repaymentFrequency: RepaymentFrequency? = null,
)