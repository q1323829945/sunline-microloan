package cn.sunline.saas.rpc.pubsub.dto

import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal

data class DTOBusinessDetail(
    val agreementId: Long,
    val customerId:Long,
    var amount: BigDecimal,
    val currency: CurrencyType,
)
