package cn.sunline.saas.pdpa.dto


data class PDPAInformation(
    val personalInformation:List<PersonalInformation>,
    val corporateInformation:List<CorporateInformation>,
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