

package cn.sunline.saas.scheduler

import cn.sunline.saas.consumer_loan.job.LoanAutoRepaymentJob
import cn.sunline.saas.consumer_loan.job.LoanInvoiceJob
import cn.sunline.saas.dapr_wrapper.actor.ActorTimerService
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.dapr_wrapper.actor.request.Timer
import cn.sunline.saas.consumer_loan.job.InvoiceAccountJob
import cn.sunline.saas.invoice.service.InvoiceService
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.job.component.CalculateSchedulerTimer
import cn.sunline.saas.scheduler.job.model.SchedulerJobLog
import cn.sunline.saas.scheduler.job.model.SchedulerType
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import cn.sunline.saas.seq.Sequence
import org.joda.time.DateTime

/**
 * @title: LoanAgreementSchedulerTask
 * @description: choreography loan agreement timing jobs
 *               this task be called by reminder
 * @author Kevin-Cui
 * @date 2022/5/25 11:23
 */
enum class LoanSchedulerJobOrder(val order: Int) {
    INVOICE_ACCOUNT(1), AUTO_REPAY(2), INVOICE_JOB(3)
}

@Component
class LoanAgreementSchedulerTask(
    actorType: String = "LoanAgreementSchedulerTask", entityConfig: EntityConfig? = null
) : AbstractActor(actorType, entityConfig) {

    // timer job's interval time,the unit is minute
    private val interval: Int = 2

    @Autowired
    private lateinit var seq: Sequence

    @Autowired
    private lateinit var calculateSchedulerTimer: CalculateSchedulerTimer

    @Autowired
    private lateinit var loanAgreementService: LoanAgreementService

    @Autowired
    private lateinit var invoiceService: InvoiceService

    @Autowired
    private lateinit var invoiceAccountJob: InvoiceAccountJob

    @Autowired
    private lateinit var loanAutoRepaymentJob: LoanAutoRepaymentJob

    @Autowired
    private lateinit var loanInvoiceJob: LoanInvoiceJob

    @Autowired
    private lateinit var schedulerJobLogService: SchedulerJobLogService

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    override fun doJob(actorId: String, taskId: String) {
        val accountDate = calculateSchedulerTimer.baseDateTime()

        val agreementId = actorId.toLong()
        val agreementAggregate = loanAgreementService.retrieve(agreementId)
        val invoices = invoiceService.listInvoiceByAgreementId(agreementId, Pageable.unpaged()).toList()
        invoices.sortBy { it.invoicePeriodToDate }


        if (invoiceAccountJob.prerequisites(accountDate, invoices)) {
            createTimerJob(
                invoiceAccountJob.actorType, agreementId.toString(), accountDate.plusMinutes(
                    LoanSchedulerJobOrder.INVOICE_ACCOUNT.order * interval
                ), accountDate, taskId
            )
        }

        if (agreementAggregate.repaymentArrangement.autoRepayment && loanAutoRepaymentJob.prerequisites(invoices)) {
            createTimerJob(
                loanAutoRepaymentJob.actorType, agreementId.toString(), accountDate.plusMinutes(
                    LoanSchedulerJobOrder.AUTO_REPAY.order * interval
                ), accountDate, taskId
            )
        }
        if (loanInvoiceJob.prerequisites(invoices)) {
            createTimerJob(
                loanInvoiceJob.actorType, agreementId.toString(), accountDate.plusMinutes(
                    LoanSchedulerJobOrder.INVOICE_JOB.order * interval
                ), accountDate, taskId
            )
        }
    }


    private fun createTimerJob(
        actorType: String, actorId: String, targetDateTime: DateTime, accountDate: DateTime, taskId: String
    ) {
        val jobId = seq.nextId()
        schedulerJobLogService.save(
            SchedulerJobLog(
                seq.nextId(),
                SchedulerType.TIMER,
                targetDateTime.toDate(),
                actorId,
                actorType,
                taskId,
                accountDate.toDate()
            )
        )

        val timeDate = tenantDateTime.betweenTimes(targetDateTime)

        val dueTime = Timer(
            timeDate.hours + timeDate.days * 24,
            timeDate.minutes,
            timeDate.seconds,
            timeDate.millis
        )

        ActorTimerService.createTimer(actorType, actorId, jobId.toString(), dueTime, null)
    }

}