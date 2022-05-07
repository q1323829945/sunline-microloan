package cn.sunline.saas.party.organisation.model.dto

import cn.sunline.saas.party.organisation.model.BusinessUnitType
import cn.sunline.saas.party.organisation.model.OrganisationIdentificationType
import cn.sunline.saas.party.organisation.model.OrganizationInvolvementType

data class DTOOrganisationAdd(
    var id:Long?,
    val legalEntityIndicator:String,
    val organisationSector:String,
    var organisationRegistrationDate: String?,
    val parentOrganisationId:String?,
    val placeOfRegistration:String,
    val organisationIdentifications:List<DTOOrganisationIdentificationAdd>,
    val organizationInvolvements:List<DTOOrganizationInvolvementAdd>,
    val businessUnits:List<DTOBusinessUnitAdd>,
    var tenantId: Long?

)

data class DTOOrganisationIdentificationAdd(
    var id:Long?,
    var organisationId:Long?,
    val organisationIdentificationType: OrganisationIdentificationType,
    val organisationIdentification: String,
    var tenantId: Long?
)

data class DTOOrganizationInvolvementAdd(
    var id:Long?,
    var organisationId:Long?,
    val organizationInvolvementType: OrganizationInvolvementType,
    val partyId: Long,
    var tenantId: Long?
)

data class DTOBusinessUnitAdd(
    var id:Long?,
    var organisationId:Long?,
    val type: BusinessUnitType,
    var tenantId: Long?
)