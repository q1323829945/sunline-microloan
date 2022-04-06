package cn.sunline.saas.pdpa.factory.impl

import cn.sunline.saas.pdpa.factory.PDPAApi
import cn.sunline.saas.pdpa.modules.CorporateInformation
import cn.sunline.saas.pdpa.modules.PDPAInformation
import cn.sunline.saas.pdpa.modules.PersonalInformation

class ChinaPDPA: PDPAApi {
    override fun getPDPA(): PDPAInformation {
        //TODO:
        val personalInformationList = ArrayList<PersonalInformation>()
        personalInformationList.add(PersonalInformation("outline","outline"))
        personalInformationList.add(PersonalInformation("company","company"))
        personalInformationList.add(PersonalInformation("address","address"))
        personalInformationList.add(PersonalInformation("uens","uens"))
        personalInformationList.add(PersonalInformation("finance","finance"))
        personalInformationList.add(PersonalInformation("capital","capital"))
        personalInformationList.add(PersonalInformation("leader","leader"))
        personalInformationList.add(PersonalInformation("shareholder","shareholder"))

        val corporateInformationList = ArrayList<CorporateInformation>()
        corporateInformationList.add(CorporateInformation("name","name"))
        corporateInformationList.add(CorporateInformation("alias","alias"))
        corporateInformationList.add(CorporateInformation("name pinyin","name pinyin"))
        corporateInformationList.add(CorporateInformation("alias pinyin","alias pinyin"))
        corporateInformationList.add(CorporateInformation("gender","gender"))
        corporateInformationList.add(CorporateInformation("birth","birth"))
        corporateInformationList.add(CorporateInformation("international","international"))
        corporateInformationList.add(CorporateInformation("register address","register address"))
        corporateInformationList.add(CorporateInformation("hdb type","hdb type"))
        corporateInformationList.add(CorporateInformation("address","address"))
        corporateInformationList.add(CorporateInformation("notice","notice"))
        corporateInformationList.add(CorporateInformation("mobile phone","mobile phone"))
        corporateInformationList.add(CorporateInformation("email","email"))


        return PDPAInformation(personalInformationList, corporateInformationList,"3")
    }
}