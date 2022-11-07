package cn.sunline.saas.channel.arrangement.model.dto

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.CommissionMethodType
import java.math.BigDecimal


data class DTOChannelCommissionItemsAdd(
    val applyStatus: ApplyStatus,
    val commissionAmount: BigDecimal?,
    val commissionRatio: BigDecimal?,
    val commissionAmountRange: BigDecimal?,
    val commissionCountRange: Long?
)

data class RangeValue(
    val commissionMethodType: CommissionMethodType,
    val applyStatus: ApplyStatus,
    val lowerLimit: BigDecimal?,
    val upperLimit: BigDecimal?,
    val rangeValue: BigDecimal
)

data class DTOChannelCommissionItemsView(
    val id: String,
    val channelArrangementId: String,
    val applyStatus: ApplyStatus,
    val commissionAmount: BigDecimal?,
    val commissionRatio: BigDecimal?,
    val commissionAmountRange: BigDecimal?,
    val commissionCountRange: Long?
)