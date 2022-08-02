package cn.sunline.saas.statistics.modules.dto

import cn.sunline.saas.global.constant.Frequency
import org.joda.time.DateTime
import java.math.BigDecimal

data class DTOCommissionStatistics(
    val channel: String,
    val commissionFeatureId: Long,
    val statisticsAmount: BigDecimal,
    val amount: BigDecimal,
    val frequency: Frequency
)

data class DTOCommissionStatisticsFindParams(
    val channel: String,
    val dateTime: DateTime,
    val frequency: Frequency,
)
