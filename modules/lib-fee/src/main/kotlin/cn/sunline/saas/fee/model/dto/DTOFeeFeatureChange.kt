package cn.sunline.saas.fee.model.dto

import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.global.constant.LoanFeeType

/**
 * @title: DTOFeeFeatureAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/14 10:47
 */
data class DTOFeeFeatureChange(
    val id:Long?,
    val feeType: LoanFeeType,
    val feeMethodType: FeeMethodType,
    val feeAmount: String?,
    val feeRate: String?,
    val feeDeductType: FeeDeductType
)
