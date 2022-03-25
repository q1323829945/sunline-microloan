package cn.sunline.saas.pdpa.factory.impl

import cn.sunline.saas.pdpa.factory.PDPAApi
import cn.sunline.saas.pdpa.modules.CorporateInformation
import cn.sunline.saas.pdpa.modules.PDPAInformation
import cn.sunline.saas.pdpa.modules.PersonalInformation

class ChinaPDPA: PDPAApi {
    override fun getPDPA(): PDPAInformation {
        //TODO:
        return PDPAInformation(listOf(PersonalInformation("1","2"),PersonalInformation("3","4")), listOf(CorporateInformation("5","6"),CorporateInformation("7","8")),"3")
    }
}