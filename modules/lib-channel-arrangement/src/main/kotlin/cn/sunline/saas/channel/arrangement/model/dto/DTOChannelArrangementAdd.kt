package cn.sunline.saas.channel.arrangement.model.dto

import cn.sunline.saas.global.constant.CommissionMethodType
import cn.sunline.saas.global.constant.CommissionType
import java.math.BigDecimal


data class DTOChannelArrangementAdd(
    val commissionMethodType: CommissionMethodType,
    val commissionAmount: BigDecimal?,
    val commissionRatio: BigDecimal?,
    val commissionType: CommissionType
)

data class DTOChannelArrangementView(
    val id: String,
    val channelAgreementId: String,
    val commissionMethodType: CommissionMethodType,
    val commissionAmount: BigDecimal?,
    val commissionRatio: BigDecimal?,
    val commissionType: CommissionType
)