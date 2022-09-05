package cn.sunline.saas.loan.model.dto

import cn.sunline.saas.loan.model.enum.Ownership
import java.io.InputStream

data class DTONameInformationView(
    val lastName:String? = null,
    val firstName:String? = null,
    val middleName:String? = null,
    val suffix:String? = null
)

data class DTOAddressInformationView(
    val unit:String? = null,
    val street:String? = null,
    val village:String? = null,
    val city:String? = null,
    val province:String? = null,
    val country:String? = null,
    val zip:String? = null,
    val ownership:Ownership? = null,
    val stay: DTOStayInformationView = DTOStayInformationView(),
    val contact:DTOPhoneInformationView = DTOPhoneInformationView(),
)

data class DTOStayInformationView(
    val year:Int = 0,
    val month:Int = 0
)

data class DTOPhoneInformationView(
    val countryCode:String? = null,
    val areaCode:String? = null,
    val number:String? = null,
    val local:String? = null
)

data class DTOChannelInformationView(
    val code:String,
    val name:String
)