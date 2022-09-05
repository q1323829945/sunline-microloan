package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.loan.model.enum.Ownership
import java.io.InputStream

data class DTONameInformation(
    val lastName:String? = null,
    val firstName:String? = null,
    val middleName:String? = null,
    val suffix:String? = null
)

data class DTOAddressInformation(
    val unit:String? = null,
    val street:String? = null,
    val village:String? = null,
    val city:String? = null,
    val province:String? = null,
    val country:String? = null,
    val zip:String? = null,
    val ownership:Ownership? = null,
    val stay: DTOStayInformation? = null,
    val contact:DTOPhoneInformation? = null,
)

data class DTOStayInformation(
    val year:Int = 0,
    val month:Int = 0
)

data class DTOPhoneInformation(
    val countryCode:String? = null,
    val areaCode:String? = null,
    val number:String? = null,
    val local:String? = null
)

data class DTOChannelInformation(
    val code:String,
    val name:String
)