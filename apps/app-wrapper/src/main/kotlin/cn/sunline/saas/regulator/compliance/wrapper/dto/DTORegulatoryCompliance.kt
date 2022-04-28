package cn.sunline.saas.regulator.compliance.wrapper.dto

data class DTORegulatoryCompliance(
    val applicationId:Long,
    val customerId: Long
)


data class DTOCallBackRegulatoryCompliance(
    val applId:Long,
    val regulatoryCompliance:String
)