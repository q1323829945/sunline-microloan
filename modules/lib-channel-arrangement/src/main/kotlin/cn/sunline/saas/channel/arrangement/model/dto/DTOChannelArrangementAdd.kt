package cn.sunline.saas.channel.arrangement.model.dto

import cn.sunline.saas.global.constant.*
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull


data class DTOChannelArrangementAdd(
    val commissionMethodType: CommissionMethodType,
    val status: ApplyStatus,
    val commissionType: CommissionType = CommissionType.LOAN_APPLICATION,
    val commissionAmount: BigDecimal?,
    val commissionRatio: BigDecimal?,
    val commissionAmountRange: BigDecimal?,
    val commissionCountRange: Long?
)


data class DTOChannelArrangementView(
    val id: String,
    val channelAgreementId: String,
    val commissionMethodType: CommissionMethodType,
    val commissionType: CommissionType,
    val commissionAmount: BigDecimal?,
    val commissionRatio: BigDecimal?,
    val commissionAmountRange: BigDecimal?,
    val commissionCountRange: Long?,
    val status: ApplyStatus
)

data class RangeValue(
    val commissionMethodType: CommissionMethodType,
    val status: ApplyStatus,
    val lowerLimit: BigDecimal?,
    val upperLimit: BigDecimal?,
    val rangeValue: BigDecimal
)
