package cn.sunline.saas.underwriting.event.dto

data class DTOExecFraudEvaluation(
    val applicationId: Long,
    val partner: String,
    val customerCreditRisk: String
)
