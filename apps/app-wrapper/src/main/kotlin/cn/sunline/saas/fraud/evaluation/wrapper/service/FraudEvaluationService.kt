package cn.sunline.saas.fraud.evaluation.wrapper.service

import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.fraud.evaluation.wrapper.dto.DTOCallBackFraudEvaluation
import cn.sunline.saas.fraud.evaluation.wrapper.dto.DTOFraudEvaluation
import org.springframework.stereotype.Service

@Service
class FraudEvaluationService {


    fun getFraudEvaluation(dtoFraudEvaluation: DTOFraudEvaluation){
        //TODO:
        val fraudEvaluation = "10"
        DaprHelper.binding(
            "CALL_BACK_CUSTOMER_FRAUD_EVALUATION",
            "create",
            DTOCallBackFraudEvaluation(dtoFraudEvaluation.applicationId,fraudEvaluation)
        )
    }
}