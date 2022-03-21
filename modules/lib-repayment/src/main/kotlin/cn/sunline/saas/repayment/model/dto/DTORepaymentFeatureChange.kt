package cn.sunline.saas.repayment.model.dto

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.repayment.model.PaymentMethodType
import cn.sunline.saas.repayment.model.PrepaymentType
import cn.sunline.saas.repayment.model.RepaymentDayType
import cn.sunline.saas.repayment.model.RepaymentFrequency
import java.math.BigDecimal

/**
 * @title: DTORepaymentProductFeatureAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/11 14:26
 */
data class DTORepaymentFeatureChange(
    val id:Long?,
    val paymentMethod: PaymentMethodType,
    val frequency: RepaymentFrequency,
    val repaymentDayType: RepaymentDayType,
    val prepaymentFeatureModality: MutableList<DTOPrepaymentFeatureModalityChange>
)

data class DTOPrepaymentFeatureModalityChange (
    val id:Long?,
    val term: LoanTermType,
    val type: PrepaymentType,
    val penaltyRatio: BigDecimal?
)