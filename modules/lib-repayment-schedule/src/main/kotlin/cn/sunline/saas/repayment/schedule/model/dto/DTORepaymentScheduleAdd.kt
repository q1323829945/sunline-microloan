package cn.sunline.saas.repayment.schedule.model.dto

import java.math.BigDecimal


data class DTORepaymentScheduleAdd (
    val installment: BigDecimal,
    val interestRate: BigDecimal,
    val schedule: MutableList<DTORepaymentScheduleDetailAdd>
)

data class DTORepaymentScheduleDetailAdd(
    val period: Int,
    val repaymentDate: String,
    val installment: BigDecimal,
    val principal: String,
    val interest: BigDecimal,
)
