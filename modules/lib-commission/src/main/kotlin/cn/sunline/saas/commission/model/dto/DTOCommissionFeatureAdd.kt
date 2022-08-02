package cn.sunline.saas.commission.model.dto

import cn.sunline.saas.global.constant.CommissionMethodType
import cn.sunline.saas.global.constant.CommissionType
import java.math.BigDecimal

data class DTOCommissionFeatureAdd(
    val commissionType: CommissionType,
    val commissionMethodType: CommissionMethodType,
    val commissionAmount: BigDecimal?,
    val commissionRatio: BigDecimal?,
)