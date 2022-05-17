package cn.sunline.saas.credit.risk.wrapper.service

import cn.sunline.saas.credit.risk.wrapper.dto.DTOCallBackCreditRisk
import cn.sunline.saas.credit.risk.wrapper.dto.DTOCreditRisk
import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import org.springframework.stereotype.Service

@Service
class CreditRiskService {


    fun getCreditRisk(dtoCreditRisk: DTOCreditRisk){
        //TODO:
        val creditRisk = "10"

        PubSubService.publish(
            "app-loan-management",
            "CALL_BACK_CREDIT_RISK",
            DTOCallBackCreditRisk(dtoCreditRisk.applicationId,creditRisk)
        )
    }
}