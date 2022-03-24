package cn.sunline.saas.pdpa.modules.dto


data class PDPAInformationView (
    val personalInformation:PersonalInformation,
    val corporateInformation:CorporateInformation,
    val pdpaTemplateId:String
)


data class PersonalInformation(
        val key:String,
        val name:String
)

data class CorporateInformation(
        val key: String,
        val name: String
)