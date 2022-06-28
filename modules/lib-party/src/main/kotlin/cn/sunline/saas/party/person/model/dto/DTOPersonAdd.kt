package cn.sunline.saas.party.person.model.dto

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.party.person.model.PersonIdentificationType
import cn.sunline.saas.party.person.model.ResidentialStatus
import cn.sunline.saas.party.person.model.RoleType

data class DTOPersonAdd(
    val personName:DTOPersonNameAdd,
    val residentialStatus: ResidentialStatus,
    val birthDate:String,
    val nationality: CountryType,
    val ethnicity:String,
    val personIdentifications:List<DTOPersonIdentificationAdd>,
    val personRoles:List<DTOPersonRoleAdd>
)

data class DTOPersonNameAdd(
    var id:Long?,
    val firstName:String,
    val familyName:String?,
    val givenName:String,
)

data class DTOPersonIdentificationAdd(
    var id:Long?,
    var personId:Long?,
    val personIdentificationType: PersonIdentificationType,
    val personIdentification: String
)

data class DTOPersonRoleAdd(
    var id:Long?,
    var personId:Long?,
    val type: RoleType
)
