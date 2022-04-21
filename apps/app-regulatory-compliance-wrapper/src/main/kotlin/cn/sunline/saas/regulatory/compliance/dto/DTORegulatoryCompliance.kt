package cn.sunline.saas.regulatory.compliance.dto

data class DTORegulatoryCompliance(
    val applicationId:Long,
    val partner: String,
    val customerId: Long
)


data class DTOCallBackRegulatoryCompliance(
    val applId:Long,
    val regulatoryCompliance:String
)