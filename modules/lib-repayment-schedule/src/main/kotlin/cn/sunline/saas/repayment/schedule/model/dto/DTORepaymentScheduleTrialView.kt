package cn.sunline.saas.repayment.schedule.model.dto

import java.math.BigDecimal

data class DTORepaymentScheduleTrialView(
//    val repaymentScheduleId: Long,
    val installment: BigDecimal? = null,
    val interestRate: BigDecimal,
    val schedule: MutableList<DTORepaymentScheduleDetailTrialView>
)

data class DTORepaymentScheduleDetailTrialView(
//    val id: Long,
    val period: Int,
    val repaymentDate: String,
    val installment: BigDecimal,
    val principal: BigDecimal,
    val interest: BigDecimal,
    val remainPrincipal : BigDecimal
)
