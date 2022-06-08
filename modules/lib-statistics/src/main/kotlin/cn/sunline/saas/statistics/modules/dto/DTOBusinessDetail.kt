package cn.sunline.saas.statistics.modules.dto

import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal
import java.util.*

data class DTOBusinessDetail(
    val agreementId: Long,
    val customerId:Long,
    var amount: BigDecimal,
    val currency: CurrencyType,
)

data class DTOBusinessDetailQueryParams(
    val startDateTime: Date,
    val endDateTime: Date,
)

data class DTOBusinessCount(
    val customerId:Long,
    val amount:BigDecimal,
    val currency:CurrencyType,
)


