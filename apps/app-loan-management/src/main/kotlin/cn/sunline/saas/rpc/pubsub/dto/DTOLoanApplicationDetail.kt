package cn.sunline.saas.rpc.pubsub.dto

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal


data class DTOLoanApplicationDetail(
    val channel: String,
    val productId: Long,
    val productName: String,
    val applicationId: Long,
    val amount: BigDecimal,
    val currency: CurrencyType,
    val status: ApplyStatus,
)
