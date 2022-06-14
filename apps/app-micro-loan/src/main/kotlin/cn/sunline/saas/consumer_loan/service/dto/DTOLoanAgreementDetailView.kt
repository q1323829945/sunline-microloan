package cn.sunline.saas.consumer_loan.service.dto

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.PaymentMethodType


data class DTOLoanAgreementDetailView (
    val agreementId: String,
    val productName: String,
    val amount: String,
    val term: LoanTermType,
    val disbursementAccount: String,
    val purpose: String?,
    val paymentMethod: PaymentMethodType,
    val disbursementBank: String,
    val agreementDocumentId: String?
)
