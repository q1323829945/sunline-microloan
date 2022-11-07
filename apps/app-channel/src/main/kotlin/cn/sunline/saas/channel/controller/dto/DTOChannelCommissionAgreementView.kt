package cn.sunline.saas.channel.controller.dto

import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelArrangementAdd
import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelArrangementView
import cn.sunline.saas.global.constant.AgreementType
import cn.sunline.saas.channel.party.organisation.model.ChannelCastType
import cn.sunline.saas.global.constant.ApplyStatus
import java.math.BigDecimal

data class DTOChannelCommissionAgreementView(
    val id: String,
    val channelId: String,
    val agreementType: AgreementType,
    val channelArrangement: DTOChannelArrangementView,
    val fromDateTime: String,
    val toDateTime: String
)

data class DTOChannelAgreementTypeView(
    val agreementType: AgreementType
)

data class ChannelCastView(
    val name: ChannelCastType,
    val parentName: ChannelCastType?,
    val id: Int
)

data class ResultChannelCastType(
    val name: ChannelCastType,
    val parentName: ChannelCastType?,
    var children: List<ResultChannelCastType>?,
    val id: Int
)