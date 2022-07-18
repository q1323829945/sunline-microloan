package cn.sunline.saas.fee.arrangement.model.dto

import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.global.constant.LoanFeeType
import java.math.BigDecimal

/**
 * @title: DTOFeeArrangementAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/25 8:44
 */
data class DTOFeeArrangementAdd(
    val feeType: LoanFeeType,
    val feeMethodType: FeeMethodType,
    val feeAmount: BigDecimal?,
    val feeRate: BigDecimal?,
    val feeDeductType: FeeDeductType
)

data class DTOFeeArrangementView(
    val id: String,
    val feeType: LoanFeeType,
    val feeMethodType: FeeMethodType,
    val feeAmount: BigDecimal?,
    val feeRate: BigDecimal?,
    val feeDeductType: FeeDeductType
)
