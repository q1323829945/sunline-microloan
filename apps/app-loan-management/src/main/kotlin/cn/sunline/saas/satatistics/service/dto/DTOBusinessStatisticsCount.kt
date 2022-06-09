package cn.sunline.saas.satatistics.service.dto

import java.math.BigDecimal

data class DTOBusinessStatisticsCount(
    val paymentAmount:BigDecimal,
    val repaymentAmount:BigDecimal,
    val tenantId:Long,
)
