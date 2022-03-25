package cn.sunline.saas.pdpa.factory.impl

import cn.sunline.saas.pdpa.factory.PDPAApi
import cn.sunline.saas.pdpa.modules.CorporateInformation
import cn.sunline.saas.pdpa.modules.PDPAInformation
import cn.sunline.saas.pdpa.modules.PersonalInformation

class ChinaPDPA: PDPAApi {
    override fun getPDPA(): PDPAInformation {
        //TODO:
        return PDPAInformation(PersonalInformation("1","2"), CorporateInformation("1","2"),"3")
    }
}