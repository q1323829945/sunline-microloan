package cn.sunline.saas.fee.model.dto

import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import java.math.BigDecimal

/**
 * @title: DTOFeeFeatureAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/14 10:47
 */
data class DTOFeeFeatureAdd(
    val feeType: String,
    val feeMethodType: FeeMethodType,
    val feeAmount: String?,
    val feeRate: String?,
    val feeDeductType: FeeDeductType
)
