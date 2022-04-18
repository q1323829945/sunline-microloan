package cn.sunline.saas.underwriting.controllers.dto

data class DTOCreditRisk(
    val data:DTOCreditRiskData
)


data class DTOCreditRiskData(
    val applId:Long,
    val creditRisk:String,
)
