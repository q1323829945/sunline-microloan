package cn.sunline.saas.invoice.service

import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.invoice.exception.LoanInvoiceBusinessException
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.dto.*
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.job.component.CalculateSchedulerTimer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class LoanInvoiceService(private val tenantDateTime: TenantDateTime) {

    @Autowired
    private lateinit var invoiceService: InvoiceService

    @Autowired
    private lateinit var loanAgreementService: LoanAgreementService

    @Autowired
    private lateinit var calculateSchedulerTimer: CalculateSchedulerTimer

    fun calculate(invoiceId: Long): DTOInvoiceTrailView {
        val invoice = invoiceService.getOne(invoiceId) ?: throw  LoanInvoiceBusinessException("Loan Invoice Not Found")
        val lines = ArrayList<DTOInvoiceLinesView>()
        invoice.invoiceLines.forEach {
            lines += DTOInvoiceLinesView(
                invoiceAmountType = it.invoiceAmountType,
                invoiceAmount = it.invoiceAmount.toPlainString()
            )
        }
        val invoiceTotalAmount = invoiceService.calculateRepaymentAmountById(invoiceId)
        return DTOInvoiceTrailView(
            invoicee = invoice.invoicee.toString(),
            invoiceId = invoiceId.toString(),
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
        //val accountDate = calculateSchedulerTimer.baseDateTime()
        val accountDate = tenantDateTime.now().plusMonths(1)
        val mapPage = page.filter {
            val invoiceRepaymentDate = tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(it.invoiceRepaymentDate))
            it.invoiceStatus != InvoiceStatus.FINISHED && it.invoiceStatus != InvoiceStatus.CANCEL && it.invoiceStatus != InvoiceStatus.TEMP  && invoiceRepaymentDate <= tenantDateTime.getYearMonthDay(accountDate)
        }.sortedByDescending { it.invoiceRepaymentDate }.map {
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
                invoiceRepaymentDate = invoice.invoiceRepaymentDate.toString(),
                invoiceLines = lines
            )
        }
        return mapPage
    }

    fun getInstalmentSchedule(agreementId: Long): DTOInvoiceScheduleView {
        val scheduleLines = ArrayList<DTOInvoiceScheduleLineView>()
        val loanAgreement = loanAgreementService.retrieve(agreementId)
        invoiceService.listInvoiceByAgreementId(agreementId, Pageable.unpaged()).map {
            scheduleLines.add(
                DTOInvoiceScheduleLineView(
                    invoiceId = it.id.toString(),
                    invoiceInstalment = it.invoiceAmount.toPlainString(),
                    invoicePrincipal = it.invoiceLines.first { lines -> lines.invoiceAmountType == InvoiceAmountType.PRINCIPAL }.invoiceAmount.toPlainString(),
                    invoiceInterest = it.invoiceLines.first { lines -> lines.invoiceAmountType == InvoiceAmountType.INTEREST }.invoiceAmount.toPlainString(),
                    invoicePeriodFromDate = it.invoicePeriodFromDate.toString(),
                    invoicePeriodToDate = it.invoicePeriodToDate.toString()
                )
            )
        }
        return DTOInvoiceScheduleView(
            agreementId = agreementId.toString(),
            repaymentFrequency = loanAgreement.repaymentArrangement.frequency,
            repaymentDayType = loanAgreement.repaymentArrangement.repaymentDayType,
            paymentMethodType = loanAgreement.repaymentArrangement.paymentMethod,
            totalInstalment = scheduleLines.sumOf { it.invoiceInstalment.toBigDecimal() }.toPlainString(),
            totalInterest = scheduleLines.sumOf { it.invoiceInterest.toBigDecimal() }.toPlainString(),
            scheduleLines = scheduleLines
        )
    }

}