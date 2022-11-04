package cn.sunline.saas.channel.statistics.modules.dto

import cn.sunline.saas.global.constant.Frequency
import org.joda.time.DateTime
import java.math.BigDecimal

data class DTOLoanApplicationStatistics(
    val channelCode: String,
    val channelName: String,
    val productId: Long,
    val productName: String,
    val applyCount: Long,
    val approvalCount: Long,
    val applyAmount: BigDecimal,
    val approvalAmount: BigDecimal,
    val frequency: Frequency,
    val dateTime: DateTime
)

data class DTOLoanApplicationStatisticsFindParams(
    val channelCode: String,
    val channelName: String,
    val productId: Long,
    val dateTime: DateTime,
    val frequency: Frequency,
)
