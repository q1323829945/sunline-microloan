package cn.sunline.saas.service

import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.dto.DTOCallBackRegulatoryCompliance
import cn.sunline.saas.dto.DTORegulatoryCompliance
import org.springframework.stereotype.Service

@Service
class RegulatoryComplianceService {


    fun getRegulatoryCompliance(creditRating: DTORegulatoryCompliance){
        //TODO:
        val regulatoryCompliance = "10"

        DaprHelper.publish(
            "underwriting-pub-sub",
            "CALL_BACK_REGULATORY_COMPLIANCE",
            DTOCallBackRegulatoryCompliance(creditRating.data.applicationId,regulatoryCompliance)
        )
    }
}