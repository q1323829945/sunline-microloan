package cn.sunline.saas.rpc.pubsub.dto

import cn.sunline.saas.channel.party.organisation.model.ChannelCastType
import cn.sunline.saas.channel.party.organisation.model.OrganisationIdentificationType


data class DTOChannelData(
    val id: String?,
    val legalEntityIndicator: String,
    val organisationSector: String,
    val organisationRegistrationDate: String?,
    val placeOfRegistration: String?,
    val channelCast: DTOChannelCastData?,
    val channelIdentification: List<DTOChannelIdentificationData>,
    val tenantId: String
)

data class DTOChannelIdentificationData(
    val id: String?,
    val channelId: String?,
    val channelIdentificationType: OrganisationIdentificationType,
    val channelIdentification: String,
)

data class DTOChannelCastData(
    var id:String?,
    val channelCode: String,
    val channelName: String,
    val channelCastType: ChannelCastType
)