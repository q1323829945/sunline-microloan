package cn.sunline.saas.channel.party.organisation.model.dto

import cn.sunline.saas.channel.party.organisation.model.BusinessUnitType
import cn.sunline.saas.channel.party.organisation.model.ChannelCastType
import cn.sunline.saas.channel.party.organisation.model.OrganisationIdentificationType
import cn.sunline.saas.channel.party.organisation.model.OrganizationInvolvementType

data class DTOOrganisationAdd(
    var id:Long?,
    val legalEntityIndicator:String,
    val organisationSector:String,
    var organisationRegistrationDate: String?,
    val parentOrganisationId:String?,
    val placeOfRegistration:String,
    val organisationIdentifications:List<DTOOrganisationIdentificationAdd>,
    val organizationInvolvements:List<DTOOrganizationInvolvementAdd>?,
    val businessUnits:List<DTOBusinessUnitAdd>?,
    val channelCast: DTOChannelCastAdd?

)

data class DTOOrganisationIdentificationAdd(
    var id:Long?,
    var organisationId:Long?,
    val organisationIdentificationType: OrganisationIdentificationType,
    val organisationIdentification: String,
)

data class DTOOrganizationInvolvementAdd(
    var id:Long?,
    var organisationId:Long?,
    val organizationInvolvementType: OrganizationInvolvementType,
    val partyId: Long,
)

data class DTOBusinessUnitAdd(
    var id:Long?,
    var organisationId:Long?,
    val type: BusinessUnitType,
)

data class DTOChannelCastAdd(
    var id:Long?,
    val channelCode: String,
    val channelName: String,
    val channelCastType: ChannelCastType
)