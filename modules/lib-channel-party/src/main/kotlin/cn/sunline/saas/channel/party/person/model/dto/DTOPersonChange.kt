package cn.sunline.saas.channel.party.person.model.dto

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.channel.party.person.model.PersonIdentificationType
import cn.sunline.saas.channel.party.person.model.ResidentialStatus
import cn.sunline.saas.channel.party.person.model.RoleType

data class DTOPersonChange(
    val personName: DTOPersonNameChange,
    val residentialStatus: ResidentialStatus,
    val nationality: CountryType,
    val ethnicity:String,
    val personIdentifications:List<DTOPersonIdentificationChange>,
    val personRoles:List<DTOPersonRoleChange>
)

data class DTOPersonNameChange(
    var id:String?,
    val firstName:String,
    val familyName:String?,
    val givenName:String,
)

data class DTOPersonIdentificationChange(
    var id:String?,
    var personId:String?,
    val personIdentificationType: PersonIdentificationType,
    val personIdentification: String
)

data class DTOPersonRoleChange(
    var id:String?,
    var personId:String?,
    val type: RoleType
)
