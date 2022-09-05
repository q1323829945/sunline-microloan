package cn.sunline.saas.satatistics.service.dto

import java.math.BigDecimal


data class DTOLoanApplicationStatisticsCount(
    val channelCode: String,
    val channelName: String,
    val productId: String,
    val productName: String,
    val amount: BigDecimal,
    val applyCount: Long,
    val approvalCount: Long,
    val dateTime: String
)

data class DTOLoanApplicationStatisticsCharts(
    val channelCode: String?,
    val channelName: String?,
    val productId: String?,
    val productName: String?,
    val applyCount: List<DTOLoanApplicationChartsCount>,
    val approvalCount: List<DTOLoanApplicationChartsApprovalCount>,
    val applyAmount: List<DTOLoanApplicationChartsAmount>,
)

data class DTOLoanApplicationChartsCount(
    val channelCode: String?,
    val channelName: String?,
    val productId: String?,
    val productName: String?,
    val applyCount: Long,
    val dateTime: String
)

data class DTOLoanApplicationChartsApprovalCount(
    val channelCode: String?,
    val channelName: String?,
    val productId: String?,
    val productName: String?,
    val approvalCount: Long,
    val dateTime: String
)



data class DTOLoanApplicationChartsAmount(
    val channelCode: String?,
    val channelName: String?,
    val productId: String?,
    val productName: String?,
    val amount: BigDecimal,
    val dateTime: String
)