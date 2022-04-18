package cn.sunline.saas.underwriting.controllers.dto

data class DTOFraudEvaluation(
    val data:DTOFraudEvaluationData
)


data class DTOFraudEvaluationData(
    val applId:Long,
    val fraudEvaluation:String,
)
