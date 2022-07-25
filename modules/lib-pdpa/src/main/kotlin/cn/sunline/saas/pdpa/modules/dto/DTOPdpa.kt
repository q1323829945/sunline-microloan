package cn.sunline.saas.pdpa.modules.dto

import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType


data class DTOPdpaAdd (
    val country:CountryType,
    val language: LanguageType,
    val pdpaInformation:List<DTOPdpaItem>
)

data class DTOPdpaChange (
    val pdpaInformation:List<DTOPdpaItem>
)

data class DTOPdpaView (
    val id:String,
    val country:CountryType,
    val language: LanguageType,
    var pdpaInformation:List<DTOPdpaItem>? = null
)

data class DTOPdpaItem(
    val item:String,
    val information: List<DTOPdpaInformation>
)

data class DTOPdpaInformation(
    val label:String,
    val name:String
)