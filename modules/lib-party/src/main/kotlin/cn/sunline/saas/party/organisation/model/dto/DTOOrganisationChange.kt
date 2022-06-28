package cn.sunline.saas.party.organisation.model.dto

import cn.sunline.saas.party.organisation.model.BusinessUnitType
import cn.sunline.saas.party.organisation.model.OrganisationIdentificationType
import cn.sunline.saas.party.organisation.model.OrganizationInvolvementType

data class DTOOrganisationChange(
    val organisationIdentifications:List<DTOOrganisationIdentificationChange>,
    val organizationInvolvements:List<DTOOrganizationInvolvementChange>,
    val businessUnits:List<DTOBusinessUnitChange>

)

data class DTOOrganisationIdentificationChange(
    var id:String?,
    var organisationId:String?,
    val organisationIdentificationType: OrganisationIdentificationType,
    val organisationIdentification: String,
)

data class DTOOrganizationInvolvementChange(
    var id:String?,
    var organisationId:String?,
    val organizationInvolvementType: OrganizationInvolvementType,
    val partyId: Long,
)

data class DTOBusinessUnitChange(
    var id:String?,
    var organisationId:String?,
    val type: BusinessUnitType,
)