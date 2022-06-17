package cn.sunline.saas.invoice.service.dto

import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.RepaymentStatus
import java.math.BigDecimal



data class DTOInvoiceTrailView1 (
    val invoicee: Long,
    val invoiceId: Long,
    val invoiceTotalAmount: String,
    val repaymentStatus: RepaymentStatus,
    val invoiceLines: List<DTOInvoiceLinesView1>,
)

data class DTOInvoiceInfoView1 (
    val invoicee: String,
    val invoiceId: String,
    val invoiceDueDate: String,
    val invoicePeriodFromDate: String,
    val invoicePeriodToDate: String,
    val invoiceTotalAmount: String,
    val invoiceCurrency: CurrencyType,
    val invoiceStatus: InvoiceStatus,
    val repaymentStatus: RepaymentStatus? = null,
    val agreementId: String? = null,
    val loanAgreementFromDate: String? = null,
    val invoiceRepaymentDate: String? = null,
    val invoiceLines: List<DTOInvoiceLinesView1>
)

data class DTOInvoiceLinesView1(
    val invoiceAmountType: InvoiceAmountType,
    val invoiceAmount: String
)

data class DTOInvoiceRepay1 (
    val invoices :MutableList<DTOInvoiceRepayDetails1>
)

data class DTOInvoiceRepayDetails1 (
    val amount: String,
    val invoiceId: Long
)

