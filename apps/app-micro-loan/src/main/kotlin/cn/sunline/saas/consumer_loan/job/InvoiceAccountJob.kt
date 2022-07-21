package cn.sunline.saas.consumer_loan.job

import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.formula.CalculateInterest
import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.db.Invoice
import cn.sunline.saas.global.constant.RepaymentStatus
import cn.sunline.saas.invoice.service.InvoiceService
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.job.component.CalculateSchedulerTimer
import cn.sunline.saas.scheduler.job.component.execute
import cn.sunline.saas.scheduler.job.component.succeed
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal

/**
 * @title: InvoiceAccountJob
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/23 16:25
 */
@Service
class InvoiceAccountJob(
    actorType: String = "InvoiceAccountActor", entityConfig: EntityConfig? = null
) : AbstractActor(actorType, entityConfig) {

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    @Autowired
    private lateinit var invoiceService: InvoiceService

    @Autowired
    private lateinit var schedulerJobLogService: SchedulerJobLogService

    @Autowired
    private lateinit var calculateSchedulerTimer: CalculateSchedulerTimer

    @Autowired
    private lateinit var loanAgreementService: LoanAgreementService

    private val outstanding: MutableMap<InvoiceAmountType, BigDecimal> = mutableMapOf()

    /**
     *  get last invoice, then check its repayment status
     *  when it equaled to overdue then compute penalty interest and outstanding amount
     *  when it equaled to mini payment amount then compute outstanding amount
     *  change status to finished
     */
    override fun doJob(actorId: String, jobId: String) {
        val schedulerJobLog = schedulerJobLogService.getOne(jobId.toLong())
        schedulerJobLog?.run {
            this.execute(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }

        val invoices = invoiceService.listInvoiceByAgreementId(actorId.toLong(), Pageable.unpaged()).toList()
        invoices.sortBy { it.invoicePeriodToDate }

        var baseDateTime = schedulerJobLog?.accountDate?.run {
            tenantDateTime.getYearMonthDay(
                tenantDateTime.toTenantDateTime(this).plusDays(1)
            )
        }
        if (baseDateTime == null) baseDateTime = tenantDateTime.getYearMonthDay(calculateSchedulerTimer.baseDateTime())

        var invoice: Invoice? = null
        var lastInvoice: Invoice? = null
        var i = 0
        for ((index, value) in invoices.withIndex()) {
            val invoiceDate = tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(value.invoicePeriodToDate))
            if (invoiceDate == baseDateTime) {
                i = index
                invoice = value
                break
            }
        }

        if (i > 0) {
            lastInvoice = invoices[i - 1]
        }

        val handleInvoices = mutableListOf<Invoice>()
        lastInvoice?.run {
            handleLastInvoice(
                this, actorId.toLong(), tenantDateTime.toTenantDateTime(invoice!!.invoicePeriodToDate)
            )?.run { handleInvoices.add(this) }
        }
        invoice?.run { handleInvoices.add(handleInvoiceAccount(this)) }

        invoiceService.save(handleInvoices)
        schedulerJobLog?.run {
            this.succeed(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }
    }


    fun prerequisites(baseDateTime: DateTime, invoices: List<Invoice>): Boolean {
        invoices.filter {
            val invoiceDate = tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(it.invoicePeriodToDate))
            invoiceDate == tenantDateTime.getYearMonthDay(baseDateTime.plusDays(1))
        }
        return invoices.isNotEmpty()
    }

    private fun handleLastInvoice(invoice: Invoice, agreementId: Long, invoiceDateTime: DateTime): Invoice? {
        return if (invoice.invoiceStatus == InvoiceStatus.ACCOUNTED) {
            when (invoice.repaymentStatus) {
                RepaymentStatus.OVERDUE -> {
                    handleOverDue(agreementId, invoice, invoiceDateTime)
                }
                else -> {}
            }
            invoice.invoiceStatus = InvoiceStatus.FINISHED

            invoice
        } else {
            null
        }

    }

    private fun handleInvoiceAccount(invoice: Invoice): Invoice {
        invoice.invoiceStatus = InvoiceStatus.ACCOUNTED
        invoice.invoiceLines.forEach {
            it.invoiceAmount = it.invoiceAmount.add(outstanding.getOrDefault(it.invoiceAmountType, BigDecimal.ZERO))
        }

        return invoice
    }

    private fun outstandingAmount(invoiceAmountType: InvoiceAmountType, amount: BigDecimal) {
        val balance = outstanding.getOrDefault(invoiceAmountType, BigDecimal.ZERO)
        outstanding[invoiceAmountType] = balance.add(amount)
    }

    private fun handleOverDue(agreementId: Long, invoice: Invoice, invoiceDateTime: DateTime) {
        val loanAgreementAggregate = loanAgreementService.retrieve(agreementId)

        invoice.invoiceLines.forEach {
            if (it.invoiceAmount != it.repaymentAmount) {
                outstandingAmount(it.invoiceAmountType, it.invoiceAmount.subtract(it.repaymentAmount))
            }
            if (it.invoiceAmountType == InvoiceAmountType.PRINCIPAL) {
                val penaltyPrincipal = it.invoiceAmount.subtract(it.repaymentAmount)
                //TODO
                val penaltyInterest = loanAgreementAggregate.interestArrangement.run {
                    val overDueRate = CalculateInterestRate(this.rate).calOverdueInterestRateWithPercent(
                        this.overdueInterestRatePercentage
                    )
                    CalculateInterest(penaltyPrincipal, CalculateInterestRate(overDueRate)).getDaysInterest(
                        tenantDateTime.toTenantDateTime(invoice.invoicePeriodToDate), invoiceDateTime, this.baseYearDays
                    )
                }
                outstandingAmount(InvoiceAmountType.PENALTY_INTEREST, penaltyInterest)
            }
        }
    }
}