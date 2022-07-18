package cn.sunline.saas.consumer_loan.service.dto

import java.math.BigDecimal

data class DTORepaymentScheduleTrialView(
    val installment: BigDecimal? = null,
    val fee: BigDecimal,
    val interestRate: BigDecimal,
    val schedule: MutableList<DTORepaymentScheduleDetailTrialView>
)

data class DTORepaymentScheduleDetailTrialView(
    val period: Int,
    val repaymentDate: String,
    val installment: BigDecimal,
    val principal: BigDecimal,
    val interest: BigDecimal,
    val fee: BigDecimal
)

