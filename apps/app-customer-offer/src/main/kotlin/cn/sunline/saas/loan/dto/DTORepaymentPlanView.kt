package cn.sunline.saas.loan.dto


data class DTORepaymentPlanView(
    val installment:String,
    val interestRate:String,
    val schedule:List<DTOSchedule>
)

data class DTOSchedule(
    val period:Long,
    val repaymentData:String,
    val installment:String,
    val principal:String,
    val interest:String
)
