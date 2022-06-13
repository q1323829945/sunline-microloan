package cn.sunline.saas.invoice.service

import cn.sunline.saas.invoice.exception.LoanInvoiceBusinessException
import cn.sunline.saas.invoice.service.dto.DTOInvoiceCalculateView
import cn.sunline.saas.invoice.service.dto.DTOInvoiceInfoView
import cn.sunline.saas.invoice.service.dto.DTOInvoiceLinesView
import cn.sunline.saas.invoice.service.dto.DTOInvoiceRepay
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.db.Invoice
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class LoanInvoiceService(private val tenantDateTime: TenantDateTime) {

    @Autowired
    private lateinit var invoiceService: InvoiceService

    @Autowired
    private lateinit var loanAgreementService: LoanAgreementService

    fun calculate(invoiceId: Long): DTOInvoiceCalculateView {
        val invoice = invoiceService.getOne(invoiceId) ?: throw  LoanInvoiceBusinessException("Loan Invoice Not Found")
        val lines = ArrayList<DTOInvoiceLinesView>()
        invoice.invoiceLines.forEach {
            lines += DTOInvoiceLinesView(
                invoiceAmountType = it.invoiceAmountType,
                invoiceAmount = it.invoiceAmount.toPlainString()
            )
        }
        val invoiceTotalAmount = invoiceService.calculateRepaymentAmountById(invoiceId)
        return DTOInvoiceCalculateView(
            invoicee = invoice.invoicee,
            invoiceId = invoiceId,
            invoiceTotalAmount = invoiceTotalAmount.toPlainString(),
            invoiceLines = lines
        )
    }

    fun getHistoryPage(
        customerId: Long,
        invoiceStartDate: String?,
        invoiceEndDate: String?,
        pageable: Pageable
    ): Page<DTOInvoiceInfoView> {
        val page = invoiceService.listInvoiceByInvoiceeAndDate(customerId, invoiceStartDate, invoiceEndDate, pageable)
        val pageMap = page.map { invoice ->
            val lines = ArrayList<DTOInvoiceLinesView>()
            invoice?.invoiceLines?.forEach{
                lines += DTOInvoiceLinesView(
                    invoiceAmountType = it.invoiceAmountType,
                    invoiceAmount = it.invoiceAmount.toPlainString()
                )
            }
            val agreement = loanAgreementService.getOne(invoice.agreementId) ?: throw LoanInvoiceBusinessException("Loan Agreement Not Found")
            DTOInvoiceInfoView(
                invoicee = invoice.invoicee.toString(),
                invoiceId = invoice.id.toString(),
                invoiceDueDate = invoice.invoiceRepaymentDate.toString(),//tenantDateTime.toString(),
                invoicePeriodFromDate = invoice.invoicePeriodFromDate.toString(),
                invoicePeriodToDate = invoice.invoicePeriodToDate.toString(),
                invoiceTotalAmount =  invoice.invoiceAmount.toPlainString(),
                invoiceCurrency = agreement.currency,
                invoiceStatus = invoice.invoiceStatus,
                invoiceLines = lines
            )
        }
        return pageMap
    }

    fun repay (dtoInvoiceRepay: List<DTOInvoiceRepay>): MutableList<DTOInvoiceInfoView> {
        val list = mutableListOf<DTOInvoiceInfoView>()
        list.apply {
            dtoInvoiceRepay.forEach {
                val invoice =
                    invoiceService.getOne(it.invoiceId) ?: throw  LoanInvoiceBusinessException("Loan Invoice Not Found")
                val repayResult =
                    invoiceService.repayInvoice(it.amount.toBigDecimal(), invoice, 0, tenantDateTime.now())
                val lines = ArrayList<DTOInvoiceLinesView>()
                repayResult.map {
                    lines += DTOInvoiceLinesView(
                        invoiceAmountType = it.key,
                        invoiceAmount = it.value.toPlainString()
                    )
                }
                val agreement = loanAgreementService.getOne(invoice.agreementId)
                    ?: throw LoanInvoiceBusinessException("Loan Agreement Not Found")
                DTOInvoiceInfoView(
                    invoicee = invoice.invoicee.toString(),
                    invoiceId = invoice.id.toString(),
                    invoiceDueDate = invoice.invoiceRepaymentDate.toString(),//tenantDateTime.toString(),
                    invoicePeriodFromDate = invoice.invoicePeriodFromDate.toString(),
                    invoicePeriodToDate = invoice.invoicePeriodToDate.toString(),
                    invoiceTotalAmount = invoice.invoiceAmount.toPlainString(),
                    invoiceCurrency = agreement.currency,
                    invoiceStatus = invoice.invoiceStatus,
                    invoiceLines = lines
                )
            }
        }
        return list
    }

    fun retrieveCurrent(customerId: Long): List<DTOInvoiceInfoView>? {
        val page = invoiceService.currentInvoiceByInvoicee(customerId)
        val filterPage = page.filter { it.invoiceStatus != InvoiceStatus.FINISHED }.map {
                invoice ->
            val lines = ArrayList<DTOInvoiceLinesView>()
            invoice?.invoiceLines?.forEach{
                lines += DTOInvoiceLinesView(
                    invoiceAmountType = it.invoiceAmountType,
                    invoiceAmount = it.invoiceAmount.toPlainString()
                )
            }
            val agreement = loanAgreementService.getOne(invoice.agreementId) ?: throw LoanInvoiceBusinessException("Loan Agreement Not Found")
            DTOInvoiceInfoView(
                invoicee = invoice.invoicee.toString(),
                invoiceId = invoice.id.toString(),
                invoiceDueDate = invoice.invoiceRepaymentDate.toString(),//tenantDateTime.toString(),
                invoicePeriodFromDate = invoice.invoicePeriodFromDate.toString(),
                invoicePeriodToDate = invoice.invoicePeriodToDate.toString(),
                invoiceTotalAmount =  invoice.invoiceAmount.toPlainString(),
                invoiceCurrency = agreement.currency,
                invoiceStatus = invoice.invoiceStatus,
                invoiceLines = lines
            )
        }
        return filterPage.toList()
    }
}