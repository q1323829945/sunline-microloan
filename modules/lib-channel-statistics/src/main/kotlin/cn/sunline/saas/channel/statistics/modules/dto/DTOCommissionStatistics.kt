package cn.sunline.saas.channel.statistics.modules.dto

import cn.sunline.saas.global.constant.Frequency
import org.joda.time.DateTime
import java.math.BigDecimal

data class DTOCommissionStatistics(
    val channelCode: String,
    val channelName: String,
    val commissionFeatureId: Long,
    val statisticsAmount: BigDecimal,
    val commissionAmount: BigDecimal,
    val frequency: Frequency
)

data class DTOCommissionStatisticsFindParams(
    val channelCode: String,
    val channelName: String,
    val dateTime: DateTime,
    val frequency: Frequency,
)
