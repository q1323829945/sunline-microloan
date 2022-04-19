package cn.sunline.saas.dapr_wrapper.service

import cn.sunline.saas.dapr_wrapper.service.dto.AppType
import cn.sunline.saas.dapr_wrapper.service.dto.AppType.*
import cn.sunline.saas.dapr_wrapper.service.dto.Subscription
import com.google.gson.Gson
import org.springframework.stereotype.Service

@Service
class DaprService {



    fun subscribe(appType: AppType):String{
        val subscriptions = when(appType){
            UNDERWRITING -> getUnderWritingSubscriptions()
            CUSTOMER_CREDIT_RATING -> getCustomerCreditRatingSubscriptions()
            CREDIT_RISK -> getCreditRiskSubscriptions()
            REGULATORY_COMPLIANCE -> getRegulatoryComplianceSubscriptions()
            FRAUD_EVALUATION -> getFraudEvaluationSubscriptions()
        }

        return Gson().toJson(subscriptions)
    }


    private fun getUnderWritingSubscriptions():List<Subscription>{
        val subscriptions = ArrayList<Subscription>()
        subscriptions.add(Subscription("underwriting-pub-sub", "INITIATE_UNDERWRITING", "/Underwriting/CreditRating"))
        subscriptions.add(Subscription("underwriting-pub-sub", "CALL_BACK_CUSTOMER_CREDIT_RATING", "/Underwriting/CreditRating"))
        subscriptions.add(Subscription("underwriting-pub-sub", "CALL_BACK_CREDIT_RISK", "/Underwriting/CustomerCreditRisk"))
        subscriptions.add(Subscription("underwriting-pub-sub", "CALL_BACK_REGULATORY_COMPLIANCE", "/Underwriting/RegulatoryCompliance"))
        subscriptions.add(Subscription("underwriting-pub-sub", "CALL_BACK_CUSTOMER_FRAUD_EVALUATION", "/Underwriting/FraudEvaluation"))

        return subscriptions
    }

    private fun getCustomerCreditRatingSubscriptions():List<Subscription>{
        val subscriptions = ArrayList<Subscription>()
        subscriptions.add(Subscription("underwriting-pub-sub", "RETRIEVE_CUSTOMER_CREDIT_RATING", "/CreditRating"))
        return subscriptions
    }

    private fun getCreditRiskSubscriptions():List<Subscription>{
        val subscriptions = ArrayList<Subscription>()
        subscriptions.add(Subscription("underwriting-pub-sub", "EXECUTE_CREDIT_RISK", "/CreditRisk"))
        return subscriptions
    }

    private fun getRegulatoryComplianceSubscriptions():List<Subscription>{
        val subscriptions = ArrayList<Subscription>()
        subscriptions.add(Subscription("underwriting-pub-sub", "EXECUTE_REGULATORY_COMPLIANCE", "/RegulatoryCompliance"))
        return subscriptions
    }

    private fun getFraudEvaluationSubscriptions():List<Subscription>{
        val subscriptions = ArrayList<Subscription>()
        subscriptions.add(Subscription("underwriting-pub-sub", "FRAUD_EVALUATION", "/FraudEvaluation"))
        return subscriptions
    }
}