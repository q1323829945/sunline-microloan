package cn.sunline.saas.fee.arrangement.model.dto

import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import java.math.BigDecimal

/**
 * @title: DTOFeeArrangementAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/25 8:44
 */
data class DTOFeeArrangementAdd(
    val feeType: String,
    val feeMethodType: FeeMethodType,
    val feeAmount: BigDecimal?,
    val feeRate: BigDecimal?,
    val feeDeductType: FeeDeductType
)
