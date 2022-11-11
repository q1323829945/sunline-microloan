package cn.sunline.saas.channel.party.organisation.model.dto

import cn.sunline.saas.channel.party.organisation.model.ChannelCastType
import cn.sunline.saas.channel.party.organisation.model.OrganisationIdentificationType

data class DTOChannelAdd(
    val legalEntityIndicator: String,
    val organisationSector: String,
    val organisationRegistrationDate: String,
    val placeOfRegistration: String,
    var channelCast: DTOChannelCastAdd,
    var channelIdentification: List<DTOChannelIdentificationAdd>
)

data class DTOChannelIdentificationAdd(
    val channelIdentificationType: OrganisationIdentificationType,
    val channelIdentification: String,
)

data class DTOChannelCastAdd(
    var id:Long?,
    val channelCode: String,
    val channelName: String,
    val channelCastType: ChannelCastType
)