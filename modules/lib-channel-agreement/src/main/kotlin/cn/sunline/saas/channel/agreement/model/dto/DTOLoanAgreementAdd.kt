package cn.sunline.saas.channel.agreement.model.dto

import cn.sunline.saas.channel.agreement.model.db.ChannelAgreement
import cn.sunline.saas.channel.arrangement.model.db.ChannelArrangement
import cn.sunline.saas.channel.arrangement.model.dto.DTOChannelArrangementAdd
import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.global.constant.AgreementType
import cn.sunline.saas.global.constant.CommissionMethodType
import cn.sunline.saas.global.constant.CommissionType
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


data class DTOChannelAgreementAdd(
    val channelId: Long,
    val agreementType: AgreementType,
    val fromDateTime: String?,
    val toDateTime: String?,
    val channelCommissionArrangement: List<DTOChannelArrangementAdd>
)


data class DTOChannelAgreementView(
    val channelAgreement: ChannelAgreement,
    val channelArrangement: List<ChannelArrangement>
)


data class DTOChannelAgreementPageView(
    val id: String,
    val channelId: String,
    val agreementType: AgreementType,
    val signedDate: String,
    val fromDateTime: String,
    val toDateTime: String
)