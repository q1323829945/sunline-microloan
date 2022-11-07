package cn.sunline.saas.channel.arrangement.model.dto

import cn.sunline.saas.global.constant.*
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull


data class DTOChannelArrangementAdd(
    val commissionMethodType: CommissionMethodType,
    val commissionType: CommissionType = CommissionType.LOAN_APPLICATION,
    val channelArrangementType: ChannelArrangementType = ChannelArrangementType.COMMISSION,
    val commissionItems: MutableList<DTOChannelCommissionItemsAdd>
)


data class DTOChannelArrangementView(
    val id: String,
    val channelAgreementId: String,
    val commissionMethodType: CommissionMethodType,
    val commissionType: CommissionType = CommissionType.LOAN_APPLICATION,
    val channelArrangementType: ChannelArrangementType = ChannelArrangementType.COMMISSION,
    val commissionItems: MutableList<DTOChannelCommissionItemsView>
)

