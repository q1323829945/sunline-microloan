package cn.sunline.saas.channel.statistics.modules.dto

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal
import java.util.*


data class DTOCommissionDetail(
    val channelCode: String,
    val channelName: String,
    val applicationId: Long,
    val commissionAmount: BigDecimal,
    val ratio: BigDecimal?,
    val statisticsAmount: BigDecimal,
    val currency: CurrencyType,
    val status: ApplyStatus,
)

data class DTOCommissionDetailQueryParams(
    val startDateTime: Date,
    val endDateTime: Date,
)

data class DTOCommissionCount(
    val channelCode: String,
    val channelName: String,
    val commissionAmount: BigDecimal,
    val statisticsAmount: BigDecimal,
)
