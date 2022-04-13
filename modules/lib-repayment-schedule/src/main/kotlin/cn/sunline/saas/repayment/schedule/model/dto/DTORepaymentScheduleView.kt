package cn.sunline.saas.repayment.schedule.model.dto

import java.math.BigDecimal


data class DTORepaymentScheduleView(
    val repaymentScheduleId: Long,
    val installment: BigDecimal? = null,
    val interestRate: BigDecimal,
    var schedule: MutableList<DTORepaymentScheduleDetailView>
)

data class DTORepaymentScheduleDetailView(
    val id: Long,
    val repaymentScheduleId: Long,
    val period: Int,
    val repaymentDate: String,
    val installment: BigDecimal,
    val principal: BigDecimal,
    val interest: BigDecimal
)