package cn.sunline.saas.satatistics.service.dto

import java.math.BigDecimal


data class DTOCommissionStatisticsCount(
    val channel: String,
    val commissionFeatureId: Long,
    val ratio: BigDecimal,
    val amount: BigDecimal,
    val statisticsAmount: BigDecimal,
    val dateTime: String
)

