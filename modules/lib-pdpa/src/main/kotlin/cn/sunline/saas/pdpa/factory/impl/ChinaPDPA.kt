package cn.sunline.saas.pdpa.factory.impl

import cn.sunline.saas.pdpa.factory.PDPAApi
import cn.sunline.saas.pdpa.modules.dto.CorporateInformation
import cn.sunline.saas.pdpa.modules.dto.PDPAInformationView
import cn.sunline.saas.pdpa.modules.dto.PersonalInformation

class ChinaPDPA:PDPAApi {
    override fun getPDPA(): PDPAInformationView {
        //TODO:
        return PDPAInformationView(PersonalInformation("1","2"), CorporateInformation("1","2"),"3")
    }
}