package cn.sunline.saas.consumer_loan.service.dto

import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.global.constant.AgreementType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.InvoiceType
import cn.sunline.saas.invoice.model.RepaymentStatus

//data class DTORepaymentAgreementView(
//    val id:String,
//    val agreementType: AgreementType,
//    val signedDate:String,
//    val fromDateTime:String,
//    val toDateTime:String,
//    val term: LoanTermType,
//    val version: Int,
//    val status: AgreementStatus,
//    val amount: String,
//    val currency: CurrencyType,
//    val productId: String,
//    val agreementDocument: String?,
//    val purpose: String?,
//    val applicationId: String,
//    val userId: String,
//    val repaymentStatus: RepaymentStatus,
//    val involvements: List<LoanAgreementInvolvementView>,
//)
//
//data class LoanAgreementInvolvementView(
//    val id:String,
//    val partyId: String,
//    val involvementType: String, //LoanAgreementInvolvementType
//)

data class DTOInvoicePage(
    val id:String,
    val agreementId: String,
    val invoiceType: InvoiceType,
    val fromDateTime:String,
    val toDateTime:String,
    val invoiceStatus: InvoiceStatus,
    val amount: String,
    val currency: CurrencyType?,
    val userId: String,
    val repaymentStatus: RepaymentStatus
)
