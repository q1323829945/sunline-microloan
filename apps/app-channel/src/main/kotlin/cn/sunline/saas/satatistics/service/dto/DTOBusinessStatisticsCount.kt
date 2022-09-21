package cn.sunline.saas.satatistics.service.dto

import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal

data class DTOBusinessStatisticsCount(
    val paymentAmount:BigDecimal,
    val repaymentAmount:BigDecimal,
    val currency: CurrencyType?,
    val tenantId:Long
)

