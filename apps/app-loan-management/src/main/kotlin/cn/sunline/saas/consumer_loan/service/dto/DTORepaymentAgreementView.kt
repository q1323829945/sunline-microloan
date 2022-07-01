package cn.sunline.saas.consumer_loan.service.dto

import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.InvoiceType
import cn.sunline.saas.invoice.model.RepaymentStatus
import cn.sunline.saas.money.transfer.instruction.model.InstructionLifecycleStatus

data class DTOInvoiceTransferInstructionPage(
    val id: String,
    val invoiceId: String,
    val agreementId: String,
    val invoiceRepaymentDate: String,
    val invoiceTotalAmount: String,
    val invoiceCurrency: CurrencyType?,
    val invoicee: String,
    val instructionLifecycleStatus: InstructionLifecycleStatus,
    val startDateTime: String?,
    val endDateTime: String?,
    val executeDateTime: String?,
    val operator: String?
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
