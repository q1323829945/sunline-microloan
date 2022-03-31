package cn.sunline.saas.repayment.schedule.model.dto

import java.math.BigDecimal

data class DTORepaymentSchedule(
    val installment: BigDecimal,
    val interestRate: BigDecimal,
    val schedule: MutableList<DTORepaymentScheduleDetail>
)

data class DTORepaymentScheduleDetail(
    val period: Int,
    val repaymentDate: String,
    val installment: BigDecimal,
    val principal: String,
    val interest: BigDecimal,
)
