package cn.sunline.saas.satatistics.service.dto

import java.math.BigDecimal


data class DTOCommissionStatisticsPageCount(
    val channelCode: String,
    val channelName: String,
    val amount: BigDecimal,
    val statisticsAmount: BigDecimal,
    val dateTime: String
)


data class DTOCommissionStatisticsCharts(
    val channelCode: String?,
    val channelName: String?,
    val commissionAmount: List<DTOCommissionChartsAmount>,
)

data class DTOCommissionChartsAmount(
    val channelCode: String?,
    val channelName: String?,
    val amount: BigDecimal,
    val dateTime: String
)

data class DTOCommissionStatisticsCount(
    val channelCode: String,
    val channelName: String,
    val amount: BigDecimal,
    val statisticsAmount: BigDecimal,
    val tenantId:Long
)

