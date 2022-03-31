package cn.sunline.saas.repayment.schedule.model.dto

import java.math.BigDecimal

data class DTORepaymentScheduleView(
    val repaymentScheduleId: Long,
    val installment: BigDecimal,
    val interestRate: BigDecimal,
    val schedule: MutableList<DTORepaymentScheduleDetailView>
)

data class DTORepaymentScheduleDetailView(
    val id: Long,
    val period: Int,
    val repaymentDate: String,
    val installment: BigDecimal,
    val principal: BigDecimal,
    val interest: BigDecimal,
)
