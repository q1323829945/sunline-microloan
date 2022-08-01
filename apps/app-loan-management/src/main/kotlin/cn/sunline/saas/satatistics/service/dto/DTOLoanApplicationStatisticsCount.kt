package cn.sunline.saas.satatistics.service.dto

import cn.sunline.saas.global.constant.Frequency
import org.joda.time.DateTime
import java.math.BigDecimal
import java.util.*


data class DTOLoanApplicationStatisticsCount(
    val channel: String,
    val productId: String,
    val productName: String,
    val amount: BigDecimal,
    val applyCount: Long,
    val approvalCount: Long,
    val dateTime: String
)