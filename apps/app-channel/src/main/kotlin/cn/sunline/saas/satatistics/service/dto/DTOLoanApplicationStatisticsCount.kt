package cn.sunline.saas.satatistics.service.dto

import java.math.BigDecimal


data class DTOLoanApplicationStatisticsCount(
    val channelCode: String,
    val channelName: String,
    val productId: String,
    val productName: String,
    val applyAmount: BigDecimal,
    val approvalAmount: BigDecimal,
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
    val applyAmount: List<DTOLoanApplicationChartsApplyAmount>,
    val approvalAmount: List<DTOLoanApplicationChartsApprovalAmount>
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



data class DTOLoanApplicationChartsApplyAmount(
    val channelCode: String?,
    val channelName: String?,
    val productId: String?,
    val productName: String?,
    val amount: BigDecimal,
    val dateTime: String
)

data class DTOLoanApplicationChartsApprovalAmount(
    val channelCode: String?,
    val channelName: String?,
    val productId: String?,
    val productName: String?,
    val amount: BigDecimal,
    val dateTime: String
)