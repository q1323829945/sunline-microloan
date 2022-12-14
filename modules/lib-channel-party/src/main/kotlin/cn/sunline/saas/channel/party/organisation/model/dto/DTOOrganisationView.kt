package cn.sunline.saas.channel.party.organisation.model.dto

import cn.sunline.saas.channel.party.organisation.model.BusinessUnitType
import cn.sunline.saas.channel.party.organisation.model.ChannelCastType
import cn.sunline.saas.channel.party.organisation.model.OrganisationIdentificationType
import cn.sunline.saas.channel.party.organisation.model.OrganizationInvolvementType
import cn.sunline.saas.global.constant.YesOrNo


data class DTOOrganisationView(
    var id:String,
    val legalEntityIndicator:String,
    val organisationSector:String,
    var organisationRegistrationDate: String?,
    val parentOrganisationId:String?,
    val placeOfRegistration:String,
    val organisationIdentifications:List<DTOOrganisationIdentificationView>,
    val organizationInvolvements:List<DTOOrganizationInvolvementView>?,
    val businessUnits:List<DTOBusinessUnitView>?,
    var tenantId: Long?,
    val channelCast: DTOChannelCastView?,
    val enable: YesOrNo
)

data class DTOOrganisationIdentificationView(
    var id:String?,
    var organisationId:String?,
    val organisationIdentificationType: OrganisationIdentificationType,
    val organisationIdentification: String,
    var tenantId: Long?
)

data class DTOOrganizationInvolvementView(
    var id:String?,
    var organisationId:String?,
    val organizationInvolvementType: OrganizationInvolvementType,
    val partyId: Long,
    var tenantId: Long?
)

data class DTOBusinessUnitView(
    var id:String?,
    var organisationId:String?,
    val type: BusinessUnitType,
    var tenantId: Long?
)

data class DTOChannelCastView(
    var id:String?,
    val channelCode: String,
    val channelName: String,
    val channelCastType: ChannelCastType
)