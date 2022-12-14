package cn.sunline.saas.repayment.arrangement.model.dto

import cn.sunline.saas.global.constant.*
import java.math.BigDecimal

/**
 * @title: DTORepaymentArrangementAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/24 14:51
 */
data class DTORepaymentArrangementAdd(
    val paymentMethod: PaymentMethodType,
    val frequency: RepaymentFrequency,
    val repaymentDayType: RepaymentDayType,
    val prepaymentArrangement: MutableList<DTOPrepaymentArrangementAdd>,
    val repaymentAccount: MutableList<DTORepaymentAccount>
)

data class DTOPrepaymentArrangementAdd(
    val term: LoanTermType,
    val type: PrepaymentType,
    val penaltyRatio: String
)

data class DTORepaymentAccount(
    val repaymentAccount: String,
    val repaymentAccountBank: String
)


data class DTORepaymentAccountAdd(
    val id: Long,
    val repaymentAccount: String,
    val repaymentAccountBank: String
)
