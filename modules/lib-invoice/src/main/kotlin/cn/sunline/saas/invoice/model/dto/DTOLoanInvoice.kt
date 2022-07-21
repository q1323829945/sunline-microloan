package cn.sunline.saas.invoice.model.dto

import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.global.constant.RepaymentStatus
import java.math.BigDecimal

/**
 * @title: DTOLoanInvoice
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/25 17:05
 */
data class DTOLoanInvoice(
    val period :Int,
    val invoicePeriodFromDate: String,
    val invoicePeriodToDate: String,
    val invoicee: Long,
    val principal: BigDecimal,
    val interest: BigDecimal,
    val fee: BigDecimal?,
    val agreementId: Long,
    val invoiceStatus: InvoiceStatus?,
)

data class DTOInvoiceTrailView(
    val invoicee: String,
    val invoiceId: String,
    val invoiceTotalAmount: String,
    val repaymentStatus: RepaymentStatus,
    val invoiceLines: MutableList<DTOInvoiceLinesView>?,
)

data class DTOInvoiceInfoView(
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
    val invoiceLines: MutableList<DTOInvoiceLinesView>
)

data class DTOInvoiceLinesView(
    val invoiceAmountType: InvoiceAmountType,
    val invoiceAmount: String
)

data class DTOPreRepaymentTrailView(
    val agreementId: String,
    val totalAmount: String,
    val prepaymentLines: MutableList<DTOInvoiceLinesView>,
)


data class DTOInvoiceRepay(
    val repaymentAccountId: String,
    val repaymentAccount: String,
    val amount: String,
    val invoiceId: String,
    val currency: CurrencyType
)

data class DTOInvoiceScheduleView(
    val agreementId: String,
    val repaymentFrequency: RepaymentFrequency,
    val repaymentDayType: RepaymentDayType,
    val paymentMethodType: PaymentMethodType,
    val fromDate: String,
    val endDate: String,
    val totalInstalmentLines: MutableList<DTOInvoiceLinesView>,
    val scheduleLines: MutableList<DTOInvoiceScheduleLineView>
)

data class DTOInvoiceScheduleLineView(
    val period :Int,
    val invoiceId: String,
    val invoiceInstalment: String,
    val invoicePeriodFromDate: String,
    val invoicePeriodToDate: String,
    val invoiceRepaymentDate: String,
    val invoiceLines: MutableList<DTOInvoiceLinesView>,
)