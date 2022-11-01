package cn.sunline.saas.channel.statistics.modules.dto

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.model.CurrencyType
import org.joda.time.DateTime
import java.math.BigDecimal
import java.util.*
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.validation.constraints.NotNull


data class DTOCommissionDetail(
    val channelCode: String,
    val channelName: String,
    val applicationId: Long,
    val commissionAmount: BigDecimal,
    val ratio: BigDecimal?,
    val statisticsAmount: BigDecimal,
    val currency: CurrencyType,
    val applyStatus: ApplyStatus,
    var dateTime: DateTime,
)

data class DTOCommissionDetailQueryParams(
    val startDateTime: Date,
    val endDateTime: Date,
)

data class DTOCommissionCount(
    val channelCode: String,
    val channelName: String,
    val commissionAmount: BigDecimal,
    val statisticsAmount: BigDecimal,
    val applyStatus: ApplyStatus,
)
