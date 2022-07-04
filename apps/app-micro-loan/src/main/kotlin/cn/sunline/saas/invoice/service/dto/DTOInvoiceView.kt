package cn.sunline.saas.invoice.service.dto

import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import java.math.BigDecimal



data class DTOInvoiceCalculateView (
    val invoicee: Long,
    val invoiceId: Long,
    val invoiceTotalAmount: String,
    val invoiceLines: List<DTOInvoiceLinesView>,
)

data class DTOInvoiceInfoView (
    val invoicee: String,
    val invoiceId: String,
    val invoiceDueDate: String,
    val invoicePeriodFromDate: String,
    val invoicePeriodToDate: String,
    val invoiceTotalAmount: String,
    val invoiceCurrency: CurrencyType,
    val invoiceStatus: InvoiceStatus,
    val invoiceLines: List<DTOInvoiceLinesView>
    )

data class DTOInvoiceLinesView(
    val invoiceAmountType: InvoiceAmountType,
    val invoiceAmount: String
)

data class DTOInvoiceRepay (
    val amount: String,
    val invoiceId: Long
)


