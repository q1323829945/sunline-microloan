package cn.sunline.saas.regulatory.compliance.service

import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.regulatory.compliance.dto.DTOCallBackRegulatoryCompliance
import cn.sunline.saas.regulatory.compliance.dto.DTORegulatoryCompliance
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