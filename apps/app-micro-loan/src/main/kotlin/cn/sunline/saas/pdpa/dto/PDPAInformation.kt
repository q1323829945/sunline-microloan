package cn.sunline.saas.pdpa.dto


data class PDPAInformation(
    val personalInformation:List<PersonalInformation>? = null,
    val corporateInformation:List<CorporateInformation>? = null,
    val pdpaTemplateId:String? = null
)