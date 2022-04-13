package cn.sunline.saas.repayment.schedule.model.dto

import cn.sunline.saas.global.constant.LoanTermType
import java.math.BigDecimal


data class DTORepaymentScheduleAdd (
    val installment: BigDecimal,
    val interestRate: BigDecimal,
    val term: LoanTermType,
    val schedule: MutableList<DTORepaymentScheduleDetailAdd>
)

data class DTORepaymentScheduleDetailAdd(
    val period: Int,
    val repaymentDate: String,
    val installment: BigDecimal,
    val principal: BigDecimal,
    val interest: BigDecimal,
)
