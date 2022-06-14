package cn.sunline.saas.consumer_loan.service.dto

import cn.sunline.saas.global.constant.*
import cn.sunline.saas.repayment.arrangement.model.db.RepaymentAccount
import java.math.BigDecimal

data class DTORepaymentArrangementView(
//    val id: String,
//    val paymentMethod: PaymentMethodType,
//    val frequency: RepaymentFrequency,
//    val repaymentDayType: RepaymentDayType,
//    val prepayment: MutableList<DTOPrepaymentArrangementView>,
    val repaymentAccounts: MutableList<DTORepaymentAccountView>,
//    var autoRepayment:Boolean
)

data class DTOPrepaymentArrangementView(
    val id: String,
    val term: LoanTermType,
    val type: PrepaymentType,
    val penaltyRatio: BigDecimal
)

data class DTORepaymentAccountView(
    val id: String,
    val repaymentAccount: String,
    val repaymentAccountBank: String,
    var status : YesOrNo
)