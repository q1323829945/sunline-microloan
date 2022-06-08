package cn.sunline.saas.statistics.modules.dto

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal


data class DTOBusinessStatistics(
    val customerId:Long,
    val amount:BigDecimal,
    val currencyType: CurrencyType,
    val frequency: Frequency,
)