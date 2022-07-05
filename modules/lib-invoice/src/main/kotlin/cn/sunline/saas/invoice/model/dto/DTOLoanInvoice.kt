package cn.sunline.saas.invoice.model.dto

import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.RepaymentStatus
import java.math.BigDecimal

/**
 * @title: DTOLoanInvoice
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/25 17:05
 */
data class DTOLoanInvoice(
    val invoicePeriodFromDate: String,
    val invoicePeriodToDate: String,
    val invoicee: Long,
    val principal: BigDecimal,
    val interest: BigDecimal,
    val fee: BigDecimal?,
    val agreementId:Long,
    val invoiceStatus: InvoiceStatus?,
)

data class DTOInvoiceTrailView (
    val invoicee: String,
    val invoiceId: String,
    val invoiceTotalAmount: BigDecimal,
    val repaymentStatus: RepaymentStatus,
    val invoiceLines: List<DTOInvoiceLinesView>?,
)

data class DTOInvoiceInfoView (
    val invoicee: String,
    val invoiceId: String,
    val invoiceDueDate: String,
    val invoicePeriodFromDate: String,
    val invoicePeriodToDate: String,
    val invoiceTotalAmount: BigDecimal,
    val invoiceCurrency: CurrencyType,
    val invoiceStatus: InvoiceStatus,
    val repaymentStatus: RepaymentStatus? = null,
    val agreementId: String? = null,
    val loanAgreementFromDate: String? = null,
    val invoiceRepaymentDate: String? = null,
    val invoiceLines: List<DTOInvoiceLinesView>
)

data class DTOInvoiceLinesView(
    val invoiceAmountType: InvoiceAmountType,
    val invoiceAmount: String
)

data class DTOPreRepaymentTrailView (
    val agreementId: String,
    val totalAmount: String,
    val prepaymentLines: List<DTOInvoiceLinesView>,
)


data class DTOInvoiceRepay (
    val repaymentAccountId: String,
    val repaymentAccount: String,
    val amount: String,
    val invoiceId: String,
    val currency: CurrencyType
)

//data class DTOInvoiceRepayDetails (
//    val repaymentAccountId: String,
//    val repaymentAccount: String,
//    val amount: String,
//    val invoiceId: String
//)

