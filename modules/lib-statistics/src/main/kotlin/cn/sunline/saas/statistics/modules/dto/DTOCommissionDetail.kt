package cn.sunline.saas.statistics.modules.dto

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal
import java.util.*
import javax.persistence.Column
import javax.validation.constraints.NotNull


data class DTOCommissionDetail(
    val channel: String,
    val applicationId: Long,
    val amount: BigDecimal,
    val currency: CurrencyType,
    val status: ApplyStatus,
)

data class DTOCommissionDetailQueryParams(
    val startDateTime: Date,
    val endDateTime: Date,
)

data class DTOCommissionCount(
    val channel: String,
    val amount: BigDecimal
)
