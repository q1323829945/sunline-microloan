package cn.sunline.saas.fraud.evaluation.wrapper.dto

data class DTOFraudEvaluation(
    val applicationId:Long,
    val customerId: Long
)

data class DTOCallBackFraudEvaluation(
    val applId:Long,
    val fraudEvaluation:String
)