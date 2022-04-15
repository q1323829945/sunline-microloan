package cn.sunline.saas.underwriting.event.dto

data class DTOExecRegulatoryCompliance(
    val applicationId: Long,
    val partner: String,
    val creditRisk: String
)