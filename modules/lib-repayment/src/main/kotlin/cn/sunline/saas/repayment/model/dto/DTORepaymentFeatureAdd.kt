package cn.sunline.saas.repayment.model.dto

import cn.sunline.saas.global.constant.*
import java.math.BigDecimal

/**
 * @title: DTORepaymentProductFeatureAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/11 14:26
 */
data class DTORepaymentFeatureAdd(
    val paymentMethod: PaymentMethodType,
    val frequency: RepaymentFrequency,
    val repaymentDayType: RepaymentDayType,
    val prepaymentFeatureModality: MutableList<DTOPrepaymentFeatureModalityAdd>
)

data class DTOPrepaymentFeatureModalityAdd (
    val term: LoanTermType,
    val type: PrepaymentType,
    val penaltyRatio: String
)