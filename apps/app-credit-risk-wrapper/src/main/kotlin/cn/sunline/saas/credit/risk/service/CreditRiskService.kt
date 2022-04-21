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

        DaprHelper.binding(
            "CALL_BACK_CREDIT_RISK",
            "create",
            DTOCallBackCreditRisk(dtoCreditRisk.applicationId,creditRisk)
        )
    }
}