package cn.sunline.saas.channel.arrangement.model.dto

import cn.sunline.saas.global.constant.CommissionAmountRangeType
import cn.sunline.saas.global.constant.CommissionCountRangeType
import cn.sunline.saas.global.constant.CommissionMethodType
import cn.sunline.saas.global.constant.CommissionType
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull


data class DTOChannelArrangementAdd(
    val commissionMethodType: CommissionMethodType,
    val commissionType: CommissionType = CommissionType.LOAN_APPLICATION,
    val commissionAmount: BigDecimal?,
    val commissionRatio: BigDecimal?,
    val commissionAmountRangeType: CommissionAmountRangeType?,
    val commissionCountRangeType: CommissionCountRangeType?
)


data class DTOChannelArrangementView(
    val id: String,
    val channelAgreementId: String,
    val commissionMethodType: CommissionMethodType,
    val commissionType: CommissionType,
    val commissionAmount: BigDecimal?,
    val commissionRatio: BigDecimal?,
    val commissionAmountRangeType: CommissionAmountRangeType?,
    val commissionCountRangeType: CommissionCountRangeType?
)

data class RangeValue(
    val lowerLimit: BigDecimal?,
    val upperLimit: BigDecimal?,
    val rangeValue: BigDecimal
)
