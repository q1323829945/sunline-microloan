package cn.sunline.saas.loan.dto

import java.math.BigDecimal

data class DTORepaymentPlanView(
    val installment:BigDecimal,
    val interestRate:BigDecimal,
    val schedule:List<DTOSchedule>
)

data class DTOSchedule(
    val period:Long,
    val repaymentData:String,
    val installment:BigDecimal,
    val principal:BigDecimal,
    val interest:BigDecimal
)
