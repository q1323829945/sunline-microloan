package cn.sunline.saas.dto

data class DTOFraudEvaluation(
    val data:DTOFraudEvaluationData,
)

data class DTOFraudEvaluationData(
    val applicationId:Long,
    val partner: String,
    val customerId: Long
)

data class DTOCallBackFraudEvaluation(
    val applId:Long,
    val fraudEvaluation:String
)