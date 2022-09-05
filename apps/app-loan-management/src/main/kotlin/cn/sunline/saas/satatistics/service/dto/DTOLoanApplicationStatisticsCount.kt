package cn.sunline.saas.satatistics.service.dto

import java.math.BigDecimal


data class DTOLoanApplicationStatisticsCount(
    val channel: String,
    val productId: String,
    val productName: String,
    val amount: BigDecimal,
    val applyCount: Long,
    val approvalCount: Long,
    val dateTime: String
)