package cn.sunline.saas.channel.statistics.modules.dto

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal
import java.util.*


data class DTOLoanApplicationDetail(
    val channelCode: String,
    val channelName: String,
    val productId: Long,
    val productName: String,
    val applicationId: Long,
    val amount: BigDecimal,
    val currency: CurrencyType,
    val status: ApplyStatus,
)

data class DTOLoanApplicationDetailQueryParams(
    val startDateTime: Date,
    val endDateTime: Date,
)

data class DTOLoanApplicationCount(
    val channelCode: String,
    val channelName: String,
    val productId: Long,
    val productName: String,
    val approvalCount: Long,
    val applyCount: Long,
    val amount: BigDecimal
)
