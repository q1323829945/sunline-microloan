package cn.sunline.saas.consumer_loan.job

import cn.sunline.saas.consumer_loan.service.ConsumerLoanService
import cn.sunline.saas.dapr_wrapper.actor.ActorCommand
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.global.constant.RepaymentStatus
import cn.sunline.saas.invoice.model.db.Invoice
import cn.sunline.saas.invoice.service.InvoiceService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.job.component.execute
import cn.sunline.saas.scheduler.job.component.succeed
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import org.springframework.data.domain.Pageable
import java.math.BigDecimal

/**
 * @title: LoanAutoRepaymentJob
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 14:44
 */
class LoanAutoRepaymentJob(
    private val tenantDateTime: TenantDateTime,
    private val invoiceService: InvoiceService,
    private val schedulerJobLogService: SchedulerJobLogService,
    private val consumerLoanService: ConsumerLoanService,
    actorType: String = "LoanAutoRepaymentJob",
    entityConfig: EntityConfig? = null
) :
    AbstractActor(actorType, entityConfig) {

    fun prerequisites(invoices: List<Invoice>): Boolean {
        return filterAutoRepaymentInvoices(invoices).isNotEmpty()
    }

    private fun filterAutoRepaymentInvoices(invoices:List<Invoice>):List<Invoice>{
        return invoices.filter {
            it.invoiceStatus == InvoiceStatus.ACCOUNTED && (it.repaymentStatus != RepaymentStatus.CLEAR)
        }
    }

    override fun doJob(actorId: String, jobId: String, data: ActorCommand) {
        val schedulerJobLog = schedulerJobLogService.getOne(jobId.toLong())
        schedulerJobLog?.run {
            this.execute(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }

        val invoices = invoiceService.listInvoiceByAgreementId(actorId.toLong(), Pageable.unpaged()).toList()
        invoices.sortBy { it.invoicePeriodToDate }

        val requiredRepaymentInvoices = filterAutoRepaymentInvoices(invoices)[0]
        val repaymentAmount = invoiceService.calculateRepaymentAmount(requiredRepaymentInvoices)

        if(repaymentAmount != BigDecimal.ZERO){
            //TODO automatic disburse amount from repayment account


            consumerLoanService.repayLoanInvoice(repaymentAmount,requiredRepaymentInvoices,actorId.toLong())
        }

        schedulerJobLog?.run {
            this.succeed(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }
    }

}