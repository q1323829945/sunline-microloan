package cn.sunline.saas.regulator.compliance.wrapper.service

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.regulator.compliance.wrapper.dto.DTOCallBackRegulatoryCompliance
import cn.sunline.saas.regulator.compliance.wrapper.dto.DTORegulatoryCompliance
import org.springframework.stereotype.Service

@Service
class RegulatoryComplianceService {


    fun getRegulatoryCompliance(creditRating: DTORegulatoryCompliance){
        //TODO:
        val regulatoryCompliance = "10"

        PubSubService.publish(
            "app-loan-management",
            "CALL_BACK_REGULATORY_COMPLIANCE",
            DTOCallBackRegulatoryCompliance(creditRating.applicationId,regulatoryCompliance)
        )
    }
}