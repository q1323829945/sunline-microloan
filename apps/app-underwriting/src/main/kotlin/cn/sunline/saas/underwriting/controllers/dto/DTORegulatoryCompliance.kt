package cn.sunline.saas.underwriting.controllers.dto

data class DTORegulatoryCompliance(
    val data:DTORegulatoryComplianceData
)

data class DTORegulatoryComplianceData(
    val applId: Long,
    val regulatoryCompliance: String
)