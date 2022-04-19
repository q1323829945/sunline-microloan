package cn.sunline.saas.fraud.evaluation.service

import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.fraud.evaluation.dto.DTOCallBackFraudEvaluation
import cn.sunline.saas.fraud.evaluation.dto.DTOFraudEvaluation
import org.springframework.stereotype.Service

@Service
class FraudEvaluationService {


    fun getFraudEvaluation(dtoFraudEvaluation: DTOFraudEvaluation){
        //TODO:
        val fraudEvaluation = "10"

        DaprHelper.publish(
            "underwriting-pub-sub",
            "CALL_BACK_CUSTOMER_FRAUD_EVALUATION",
            DTOCallBackFraudEvaluation(dtoFraudEvaluation.data.applicationId,fraudEvaluation)
        )
    }
}