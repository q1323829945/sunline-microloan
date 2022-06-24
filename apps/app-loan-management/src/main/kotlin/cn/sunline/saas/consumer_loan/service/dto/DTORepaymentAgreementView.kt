package cn.sunline.saas.consumer_loan.service.dto

import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.global.constant.AgreementType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.InvoiceType
import cn.sunline.saas.invoice.model.RepaymentStatus
import cn.sunline.saas.money.transfer.instruction.model.InstructionLifecycleStatus
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.validation.constraints.NotNull

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

data class DTOInvoiceTransferInstructionPage(
    val id: String,
    val invoiceId: String,
    val agreementId: String,
    val invoiceType: InvoiceType,
    val invoicePeriodFromDate: String,
    val invoicePeriodToDate: String,
    val invoiceRepaymentDate: String,
    val invoiceStatus: InvoiceStatus,
    val invoiceTotalAmount: String,
    val invoiceCurrency: CurrencyType?,
    val invoicee: String,
    val repaymentStatus: RepaymentStatus,
    val instructionLifecycleStatus: InstructionLifecycleStatus,
    val loanAgreementFromDate: String
)

data class DTOInvoicePage(
    val invoiceId: String,
    val agreementId: String,
    val invoiceType: InvoiceType,
    val invoicePeriodFromDate: String,
    val invoicePeriodToDate: String,
    val invoiceRepaymentDate: String,
    val invoiceStatus: InvoiceStatus,
    val invoiceTotalAmount: String,
    val invoiceCurrency: CurrencyType?,
    val invoicee: String,
    val repaymentStatus: RepaymentStatus
)


data class DTOInvoiceLineView(
    val Id: String,
    val invoiceId: String,
    val invoiceAmountType: InvoiceAmountType,
    val invoiceAmount: String,
    val repaymentAmount: String,
    val repaymentStatus: RepaymentStatus,
)
