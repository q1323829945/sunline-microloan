package cn.sunline.saas.rbac.service.dto

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.party.person.model.PersonIdentificationType
import cn.sunline.saas.party.person.model.ResidentialStatus
import cn.sunline.saas.party.person.model.RoleType

data class DTOPerson(
    var id:String,
    val personName:DTOPersonName,
    val residentialStatus: ResidentialStatus,
    val birthDate:String,
    val nationality: CountryType,
    val ethnicity:String,
    val personIdentifications:List<DTOPersonIdentification>,
    val personRoles:List<DTOPersonRole>
)

data class DTOPersonName(
    var id:String,
    val firstName:String,
    val familyName:String?,
    val givenName:String,
)

data class DTOPersonIdentification(
    var id:String,
    var personId:String,
    val personIdentificationType: PersonIdentificationType,
    val personIdentification: String
)

data class DTOPersonRole(
    var id:String,
    var personId:String,
    val type: RoleType
)
