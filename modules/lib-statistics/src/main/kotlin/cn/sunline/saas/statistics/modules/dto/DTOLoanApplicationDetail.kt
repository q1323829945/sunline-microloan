package cn.sunline.saas.statistics.modules.dto

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal
import java.util.*
import javax.persistence.Column
import javax.validation.constraints.NotNull


data class DTOLoanApplicationDetail(
    val channelCode: String,
    val channelName: String,
    val productId: Long,
    val productName: String,
    val applicationId: Long,
    val applyAmount: BigDecimal,
    val approvalAmount: BigDecimal,
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
    val applyAmount: BigDecimal,
    val approvalAmount: BigDecimal
)
