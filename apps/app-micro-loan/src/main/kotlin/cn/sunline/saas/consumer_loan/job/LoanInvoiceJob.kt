package cn.sunline.saas.consumer_loan.job

import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.invoice.arrangement.service.InvoiceArrangementService
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.db.Invoice
import cn.sunline.saas.invoice.service.InvoiceService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.job.component.CalculateSchedulerTimer
import cn.sunline.saas.scheduler.job.component.execute
import cn.sunline.saas.scheduler.job.component.succeed
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * @title: LoanInvoiceJob
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 14:52
 */
@Service
class LoanInvoiceJob(
    actorType: String = "LoanInvoiceJob", entityConfig: EntityConfig? = null
) : AbstractActor(actorType, entityConfig) {

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    @Autowired
    private lateinit var schedulerJobLogService: SchedulerJobLogService

    @Autowired
    private lateinit var calculateSchedulerTimer: CalculateSchedulerTimer

    @Autowired
    private lateinit var invoiceService: InvoiceService

    @Autowired
    private lateinit var invoiceArrangementService: InvoiceArrangementService

    override fun doJob(actorId: String, jobId: String) {
        val schedulerJobLog = schedulerJobLogService.getOne(jobId.toLong())
        schedulerJobLog?.run {
            this.execute(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }

        var baseDateTime = schedulerJobLog?.accountDate?.run {
            tenantDateTime.toTenantDateTime(this)
        }
        if (baseDateTime == null) baseDateTime = calculateSchedulerTimer.baseDateTime()

        val invoices = invoiceService.listInvoiceByAgreementId(actorId.toLong(), Pageable.unpaged()).toList()
        invoices.sortBy { it.invoicePeriodToDate }

        val repaymentInvoice = filterRepaymentStatusInvoices(baseDateTime, invoices).toMutableList()

        val graceDays = invoiceArrangementService.getOne(jobId.toLong())?.graceDays

        val overduePrincipal = invoiceService.overdueInvoice(repaymentInvoice, graceDays ?: 0, baseDateTime)
        // TODO increase overdue principal


        schedulerJobLog?.run {
            this.succeed(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }
    }

    fun prerequisites(accountDate: DateTime, invoices: List<Invoice>): Boolean {
        return filterRepaymentStatusInvoices(accountDate, invoices).isNotEmpty()
    }

    private fun filterRepaymentStatusInvoices(accountDate: DateTime, invoices: List<Invoice>): List<Invoice> {
        return invoices.filter {
            val invoiceRepaymentDate =
                tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(it.invoiceRepaymentDate))
            it.invoiceStatus == InvoiceStatus.ACCOUNTED && invoiceRepaymentDate <= tenantDateTime.getYearMonthDay(
                accountDate
            )
        }
    }

}