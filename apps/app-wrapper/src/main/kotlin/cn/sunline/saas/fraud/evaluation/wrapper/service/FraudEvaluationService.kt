package cn.sunline.saas.fraud.evaluation.wrapper.service

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.fraud.evaluation.wrapper.dto.DTOCallBackFraudEvaluation
import cn.sunline.saas.fraud.evaluation.wrapper.dto.DTOFraudEvaluation
import org.springframework.stereotype.Service

@Service
class FraudEvaluationService {


    fun getFraudEvaluation(dtoFraudEvaluation: DTOFraudEvaluation){
        //TODO:
        val fraudEvaluation = "10"

        PubSubService.publish(
            "app-loan-management",
            "CALL_BACK_CUSTOMER_FRAUD_EVALUATION",
            DTOCallBackFraudEvaluation(dtoFraudEvaluation.applicationId,fraudEvaluation)
        )
    }
}