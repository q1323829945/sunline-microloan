package cn.sunline.saas.scheduler

import cn.sunline.saas.consumer_loan.job.InvoiceAccountJob
import cn.sunline.saas.consumer_loan.job.LoanAutoRepaymentJob
import cn.sunline.saas.consumer_loan.job.LoanInvoiceJob
import cn.sunline.saas.dapr_wrapper.actor.ActorCommand
import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.dapr_wrapper.actor.request.Timer
import cn.sunline.saas.invoice.service.InvoiceService
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.job.component.CalculateSchedulerTimer
import cn.sunline.saas.scheduler.job.model.SchedulerJobLog
import cn.sunline.saas.scheduler.job.model.SchedulerType
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import cn.sunline.saas.seq.Sequence
import org.joda.time.DateTime
import org.springframework.data.domain.Pageable


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

class LoanAgreementSchedulerTask(
    private val seq: Sequence,
    private val calculateSchedulerTimer: CalculateSchedulerTimer,
    private val loanAgreementService: LoanAgreementService,
    private val invoiceService: InvoiceService,
    private val invoiceAccountJob: InvoiceAccountJob,
    private val loanAutoRepaymentJob: LoanAutoRepaymentJob,
    private val loanInvoiceJob: LoanInvoiceJob,
    private val schedulerJobLogService: SchedulerJobLogService,
    private val tenantDateTime: TenantDateTime,
    actorType: String = "LoanAgreementSchedulerTask", entityConfig: EntityConfig? = null
) : AbstractActor(actorType, entityConfig) {
    // timer job's interval time,the unit is minute
    private val interval: Int = 2

    override fun doJob(actorId: String, taskId: String, data: ActorCommand) {
        val accountDate = calculateSchedulerTimer.baseDateTime()

        val agreementId = actorId.toLong()
        val agreementAggregate = loanAgreementService.retrieve(agreementId)
        val invoices = invoiceService.listInvoiceByAgreementId(agreementId, Pageable.unpaged()).toList()
        invoices.sortBy { it.invoicePeriodToDate }


        if (invoiceAccountJob.prerequisites(accountDate, invoices)) {
            createReminderJob(
                invoiceAccountJob.actorType, agreementId.toString(), accountDate.plusMinutes(
                    LoanSchedulerJobOrder.INVOICE_ACCOUNT.order * interval
                ), accountDate, taskId
            )
        }

        if (agreementAggregate.repaymentArrangement.autoRepayment && loanAutoRepaymentJob.prerequisites(invoices)) {
            createReminderJob(
                loanAutoRepaymentJob.actorType, agreementId.toString(), accountDate.plusMinutes(
                    LoanSchedulerJobOrder.AUTO_REPAY.order * interval
                ), accountDate, taskId
            )
        }
        if (loanInvoiceJob.prerequisites(accountDate,invoices)) {
            createReminderJob(
                loanInvoiceJob.actorType, agreementId.toString(), accountDate.plusMinutes(
                    LoanSchedulerJobOrder.INVOICE_JOB.order * interval
                ), accountDate, taskId
            )
        }
    }


    private fun createReminderJob(
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

        ActorReminderService.createReminders(actorType, actorId, jobId.toString(), dueTime, null)
    }

}