package cn.sunline.saas.schedule.dto

import java.math.BigDecimal

data class DTORepaymentScheduleTrialView(
    val installment: BigDecimal? = null,
    val interestRate: BigDecimal,
    val schedule: MutableList<DTORepaymentScheduleDetailTrialView>
)

data class DTORepaymentScheduleDetailTrialView(
    val period: Int,
    val repaymentDate: String,
    val installment: BigDecimal,
    val principal: BigDecimal,
    val interest: BigDecimal,
)

