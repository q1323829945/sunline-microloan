package cn.sunline.saas.statistics.modules.dto

import cn.sunline.saas.global.constant.Frequency
import org.joda.time.DateTime
import java.math.BigDecimal

data class DTOLoanApplicationStatistics(
    val channel: String,
    val productId: Long,
    val productName: String,
    val amount: BigDecimal,
    val applyCount: Long,
    val approvalCount: Long,
    val frequency: Frequency
)

data class DTOLoanApplicationStatisticsFindParams(
    val channel: String,
    val productId: Long,
    val dateTime: DateTime,
    val frequency: Frequency,
)
