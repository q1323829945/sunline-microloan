package cn.sunline.saas.rpc.bindings.dto

import cn.sunline.saas.channel.party.organisation.model.ChannelCastType
import cn.sunline.saas.channel.party.organisation.model.OrganisationIdentificationType
import cn.sunline.saas.global.constant.YesOrNo


data class DTOChannelData(
    val channelCode: String,
    val channelName: String,
    val enable: YesOrNo = YesOrNo.Y,
    val isUpdate: Boolean = false
)
