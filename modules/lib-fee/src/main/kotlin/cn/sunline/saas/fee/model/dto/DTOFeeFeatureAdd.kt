package cn.sunline.saas.fee.model.dto

import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.global.constant.LoanFeeType
import java.math.BigDecimal

/**
 * @title: DTOFeeFeatureAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/14 10:47
 */
data class DTOFeeFeatureAdd(
    val feeType: LoanFeeType,
    val feeMethodType: FeeMethodType,
    val feeAmount: BigDecimal?,
    val feeRate: BigDecimal?,
    val feeDeductType: FeeDeductType
)
