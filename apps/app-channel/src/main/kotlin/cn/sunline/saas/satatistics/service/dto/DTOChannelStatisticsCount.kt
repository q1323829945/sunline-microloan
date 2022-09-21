package cn.sunline.saas.satatistics.service.dto

import cn.sunline.saas.global.constant.ApplyStatus
import java.math.BigDecimal


data class DTOChannelStatistics(
    val channelCode: String,
    val channelName: String,
    val applyStatus: ApplyStatus?,
    val count: Long?,
    val amount: BigDecimal?,
)
data class ChannelStatisticsCount(
    val applyStatus: ApplyStatus,
    val count: Long?,
)

data class ChannelStatisticsAmount(
    val applyStatus: ApplyStatus,
    val amount: BigDecimal?,
)

