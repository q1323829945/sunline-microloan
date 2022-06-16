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
import cn.sunline.saas.scheduler.job.component.CalculateSchedulerTimer
import org.joda.time.DateTime
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

    @Autowired
    private lateinit var calculateSchedulerTimer: CalculateSchedulerTimer

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
            repaymentStatus = invoice.repaymentStatus,
            invoiceLines = lines
        )
    }

    fun getHistoryPage(
        customerId: Long,
        invoiceStartDate: String?,
        invoiceEndDate: String?
    ): MutableList<DTOInvoiceInfoView> {
        val page = invoiceService.listInvoiceByInvoiceeAndDate(customerId, invoiceStartDate, invoiceEndDate)
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
                invoiceRepaymentDate = invoice.invoiceRepaymentDate.toString(),
                invoiceLines = lines
            )
        }
        return pageMap.toList()
    }

    fun retrieveCurrentAccountedInvoices(customerId: Long): List<DTOInvoiceInfoView> {
        val page = invoiceService.retrieveCurrentInvoices(customerId,null)
        val accountDate = calculateSchedulerTimer.baseDateTime()
        val mapPage = page.filter {
            val invoiceRepaymentDate = tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(it.invoiceRepaymentDate))
            it.invoiceStatus != InvoiceStatus.FINISHED && invoiceRepaymentDate <= tenantDateTime.getYearMonthDay(accountDate)
        }.sortedByDescending { it.invoiceStatus }.map {
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
                repaymentStatus = invoice.repaymentStatus,
                agreementId = invoice.agreementId.toString(),
                loanAgreementFromDate = tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(agreement.fromDateTime)),
                invoiceLines = lines
            )
        }
        return mapPage
    }

}