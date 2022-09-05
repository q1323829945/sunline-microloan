package cn.sunline.saas.channel.party.person.model.dto

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.channel.party.person.model.PersonIdentificationType
import cn.sunline.saas.channel.party.person.model.ResidentialStatus
import cn.sunline.saas.channel.party.person.model.RoleType

data class DTOPersonView(
    var id:String,
    val personName: DTOPersonNameView,
    val residentialStatus: ResidentialStatus,
    val birthDate:String,
    val nationality: CountryType,
    val ethnicity:String,
    val personIdentifications:List<DTOPersonIdentificationView>,
    val personRoles:List<DTOPersonRoleView>
)

data class DTOPersonNameView(
    var id:String,
    val firstName:String,
    val familyName:String?,
    val givenName:String,
)

data class DTOPersonIdentificationView(
    var id:String,
    var personId:String,
    val personIdentificationType: PersonIdentificationType,
    val personIdentification: String
)

data class DTOPersonRoleView(
    var id:String,
    var personId:String,
    val type: RoleType
)
