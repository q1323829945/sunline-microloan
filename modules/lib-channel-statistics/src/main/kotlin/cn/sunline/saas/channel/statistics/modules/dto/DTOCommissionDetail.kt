package cn.sunline.saas.channel.statistics.modules.dto

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal
import java.util.*
import javax.persistence.Column
import javax.validation.constraints.NotNull


data class DTOCommissionDetail(
    val channelCode: String,
    val channelName: String,
    val applicationId: String,
    val amount: BigDecimal,
    val status: ApplyStatus,
)

data class DTOCommissionDetailQueryParams(
    val startDateTime: Date,
    val endDateTime: Date,
)

data class DTOCommissionCount(
    val channelCode: String,
    val channelName: String,
    val amount: BigDecimal
)
