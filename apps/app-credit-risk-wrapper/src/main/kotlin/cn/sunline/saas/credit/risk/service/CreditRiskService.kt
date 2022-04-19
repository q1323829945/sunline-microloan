package cn.sunline.saas.credit.risk.service

import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.credit.risk.dto.DTOCallBackCreditRisk
import cn.sunline.saas.credit.risk.dto.DTOCreditRisk
import org.springframework.stereotype.Service

@Service
class CreditRiskService {


    fun getCreditRisk(dtoCreditRisk: DTOCreditRisk){
        //TODO:
        val creditRisk = "10"

        DaprHelper.publish(
            "underwriting-pub-sub",
            "CALL_BACK_CREDIT_RISK",
            DTOCallBackCreditRisk(dtoCreditRisk.data.applicationId,creditRisk)
        )
    }
}