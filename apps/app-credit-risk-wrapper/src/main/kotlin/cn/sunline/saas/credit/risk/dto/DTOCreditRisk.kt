package cn.sunline.saas.credit.risk.dto

data class DTOCreditRisk(
    val applicationId:Long,
    val customerId: Long
)


data class DTOCallBackCreditRisk(
    val applId:Long,
    val creditRisk:String
)