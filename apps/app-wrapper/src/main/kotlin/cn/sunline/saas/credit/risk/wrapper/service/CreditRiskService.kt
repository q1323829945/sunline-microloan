package cn.sunline.saas.credit.risk.wrapper.service

import cn.sunline.saas.credit.risk.wrapper.dto.DTOCallBackCreditRisk
import cn.sunline.saas.credit.risk.wrapper.dto.DTOCreditRisk
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