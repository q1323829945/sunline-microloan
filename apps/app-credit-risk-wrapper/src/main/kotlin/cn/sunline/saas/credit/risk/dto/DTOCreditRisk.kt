package cn.sunline.saas.credit.risk.dto

data class DTOCreditRisk(
    val data: DTOCreditRiskData,
)

data class DTOCreditRiskData(
    val applicationId:Long,
    val partner: String,
    val customerId: Long
)

data class DTOCallBackCreditRisk(
    val applId:Long,
    val creditRisk:String
)