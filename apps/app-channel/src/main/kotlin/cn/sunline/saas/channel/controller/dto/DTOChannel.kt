package cn.sunline.saas.channel.controller.dto

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


data class DTOChannelView(
    val id: String,
    val legalEntityIndicator: String,
    val organisationSector: String,
    val organisationRegistrationDate: String?,
    val placeOfRegistration: String?,
    val channelCast: DTOChannelCastView?,
    val channelIdentification: List<DTOChannelIdentificationView>,
    val tenantId: String
)

data class DTOChannelIdentificationView(
    val id: String,
    val channelId: String,
    val channelIdentificationType: OrganisationIdentificationType,
    val channelIdentification: String,
)

data class DTOChannelCastView(
    var id:String,
    val channelCode: String,
    val channelName: String,
    val channelCastType: ChannelCastType
)


data class DTOChannelChange(
    var channelIdentification: List<DTOChannelIdentificationChange>
)

data class DTOChannelIdentificationChange(
    var id: String?,
    var channelId: String?,
    val channelIdentificationType: OrganisationIdentificationType,
    val channelIdentification: String,
)