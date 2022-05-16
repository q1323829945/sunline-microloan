package cn.sunline.saas.regulator.compliance.wrapper.service

import cn.sunline.saas.regulator.compliance.wrapper.dto.DTOCallBackRegulatoryCompliance
import cn.sunline.saas.regulator.compliance.wrapper.dto.DTORegulatoryCompliance
import org.springframework.stereotype.Service

@Service
class RegulatoryComplianceService {


    fun getRegulatoryCompliance(creditRating: DTORegulatoryCompliance){
        //TODO:
        val regulatoryCompliance = "10"
//
//        DaprHelper.binding(
//            "CALL_BACK_REGULATORY_COMPLIANCE",
//            "create",
//            DTOCallBackRegulatoryCompliance(creditRating.applicationId,regulatoryCompliance)
//        )
    }
}