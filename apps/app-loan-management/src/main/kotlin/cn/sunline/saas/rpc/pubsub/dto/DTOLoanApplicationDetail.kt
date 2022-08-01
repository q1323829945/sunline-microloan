package cn.sunline.saas.rpc.pubsub.dto

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal
import java.util.*
import javax.persistence.Column
import javax.validation.constraints.NotNull


data class DTOLoanApplicationDetail(
    val channelId: Long,
    val productId: Long,
    val applicationId: Long,
    val amount: BigDecimal,
    val currency: CurrencyType,
    val status: ApplyStatus,
)
