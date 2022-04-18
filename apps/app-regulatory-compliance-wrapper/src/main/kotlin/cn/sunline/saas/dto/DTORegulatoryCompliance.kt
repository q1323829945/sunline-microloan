package cn.sunline.saas.dto

data class DTORegulatoryCompliance(
    val data:DTORegulatoryComplianceData,
)

data class DTORegulatoryComplianceData(
    val applicationId:Long,
    val partner: String,
    val customerId: Long
)

data class DTOCallBackRegulatoryCompliance(
    val applId:Long,
    val regulatoryCompliance:String
)