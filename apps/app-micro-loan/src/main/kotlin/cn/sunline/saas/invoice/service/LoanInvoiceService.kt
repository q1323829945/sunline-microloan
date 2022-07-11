package cn.sunline.saas.invoice.service

import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.invoice.exception.LoanInvoiceBusinessException
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.dto.*
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.job.component.CalculateSchedulerTimer
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

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
            invoice?.invoiceLines?.forEach {
                lines += DTOInvoiceLinesView(
                    invoiceAmountType = it.invoiceAmountType,
                    invoiceAmount = it.invoiceAmount.toPlainString()
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
                invoiceRepaymentDate = invoice.invoiceRepaymentDate.toString(),
                invoiceLines = lines
            )
        }
        return pageMap.toList()
    }

    fun retrieveCurrentAccountedInvoices(customerId: Long): List<DTOInvoiceInfoView> {
        val page = invoiceService.retrieveCurrentInvoices(customerId, null)
        val list = page.groupBy { it.agreementId }.map {
            val invoice = it.value.sortedBy { i -> i.invoiceRepaymentDate }.first { invoice ->
                invoice.invoiceStatus == InvoiceStatus.INITIATE || invoice.invoiceStatus == InvoiceStatus.ACCOUNTED
            }
            val lines = ArrayList<DTOInvoiceLinesView>()
            invoice.invoiceLines.forEach { invoiceLine ->
                lines += DTOInvoiceLinesView(
                    invoiceAmountType = invoiceLine.invoiceAmountType,
                    invoiceAmount = invoiceLine.invoiceAmount.toPlainString()
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
                repaymentStatus = invoice.repaymentStatus,
                agreementId = invoice.agreementId.toString(),
                loanAgreementFromDate = tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(agreement.fromDateTime)),
                invoiceRepaymentDate = invoice.invoiceRepaymentDate.toString(),
                invoiceLines = lines
            )
        }
        return list
    }

    fun getInstalmentSchedule(agreementId: Long): DTOInvoiceScheduleView {
        val scheduleLines = ArrayList<DTOInvoiceScheduleLineView>()
        val totalInstalmentLines = ArrayList<DTOInvoiceLinesView>()

        val loanAgreement = loanAgreementService.retrieve(agreementId)
        var totalInterest = BigDecimal.ZERO
        var totalFee = BigDecimal.ZERO
        invoiceService.listInvoiceByAgreementId(agreementId, Pageable.unpaged()).map {
            scheduleLines.add(
                DTOInvoiceScheduleLineView(
                    period = it.period,
                    invoiceId = it.id.toString(),
                    invoiceInstalment = it.invoiceAmount.toPlainString(),
                    invoicePeriodFromDate = it.invoicePeriodFromDate.toString(),
                    invoicePeriodToDate = it.invoicePeriodToDate.toString(),
                    invoiceRepaymentDate = it.invoiceRepaymentDate.toString(),
                    invoiceLines = objectMapper.convertValue(it.invoiceLines)
                )
            )
        }
        scheduleLines.forEach{
            view -> view.invoiceLines.forEach{
                if(it.invoiceAmountType == InvoiceAmountType.FEE){
                    totalFee = totalFee.add(it.invoiceAmount.toBigDecimal())
                }else if(it.invoiceAmountType == InvoiceAmountType.INTEREST) {
                    totalInterest = totalInterest.add(it.invoiceAmount.toBigDecimal())
                }else{

                }
            }
        }

        totalInstalmentLines.add(DTOInvoiceLinesView(
            InvoiceAmountType.FEE,
            totalInterest.toPlainString()
        ))

        totalInstalmentLines.add(DTOInvoiceLinesView(
            InvoiceAmountType.INTEREST,
            totalFee.toPlainString()
        ))

        totalInstalmentLines.add(DTOInvoiceLinesView(
            InvoiceAmountType.INSTALMENT,
            scheduleLines.sumOf { it.invoiceInstalment.toBigDecimal() }.toPlainString()
        ))

        return DTOInvoiceScheduleView(
            agreementId = agreementId.toString(),
            repaymentFrequency = loanAgreement.repaymentArrangement.frequency,// RepaymentFrequency.ONE_MONTH,// loanAgreement.repaymentArrangement.frequency,
            repaymentDayType = loanAgreement.repaymentArrangement.repaymentDayType,//RepaymentDayType.MONTH_FIRST_DAY,// loanAgreement.repaymentArrangement.repaymentDayType,
            paymentMethodType = loanAgreement.repaymentArrangement.paymentMethod,//PaymentMethodType.ONE_OFF_REPAYMENT,// loanAgreement.repaymentArrangement.paymentMethod,
//            totalInstalment = scheduleLines.sumOf { it.invoiceInstalment.toBigDecimal() }.toPlainString(),
//            totalInterest = scheduleLines.sumOf{ it.invoiceLines.forEach { lines -> lines.invoiceAmountType == InvoiceAmountType.INTEREST }.invoiceAmount }
//            totalFee = scheduleLines.sumOf { it.invoiceInterest.toBigDecimal() }.toPlainString(),
            fromDate = loanAgreement.loanAgreement.fromDateTime.toString(),
            endDate = loanAgreement.loanAgreement.toDateTime.toString(),
            totalInstalmentLines = totalInstalmentLines.toMutableList(),
            scheduleLines = scheduleLines
        )
    }

}