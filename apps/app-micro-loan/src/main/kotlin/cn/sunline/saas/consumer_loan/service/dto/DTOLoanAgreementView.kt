package cn.sunline.saas.consumer_loan.service.dto

import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.global.constant.LoanFeeType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.loan.product.model.LoanProductType
import java.math.BigDecimal

data class DTOLoanAgreementView(
    val id: String,
    val status: AgreementStatus
)

data class DTOLoanAgreementViewInfo (
    val id: String,
    val productName: String,
    val term: LoanTermType,
    val amount: String,
    val currency: CurrencyType,
    val productId: String,
    val purpose: String?,
    val applicationId: String,
    val fromDateTime: String,
    val toDateTime: String,
    val signedDate: String,
    val disbursementAccountBank: String,
    val disbursementAccount: String,
    val paymentMethod: PaymentMethodType,
    val loanProductType: LoanProductType,
    val agreementStatus: AgreementStatus
)

data class DTOFeeItemView(
    val agreementId: String,
    val loanFeeType: LoanFeeType,
    val loanFeeTypeName: String,
    val currency: CurrencyType,
    val feeAmountOrRatio: String,
    val nonPaymentAmount: String
)
