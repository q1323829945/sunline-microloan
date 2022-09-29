package cn.sunline.saas.runner

import cn.sunline.saas.account.service.LoanAccountService
import cn.sunline.saas.consumer_loan.job.InvoiceAccountJob
import cn.sunline.saas.consumer_loan.job.LoanAutoRepaymentJob
import cn.sunline.saas.consumer_loan.job.LoanInvoiceJob
import cn.sunline.saas.consumer_loan.service.ConsumerLoanService
import cn.sunline.saas.dapr_wrapper.actor.model.ActorContext
import cn.sunline.saas.invoice.service.InvoiceService
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.LoanAgreementSchedulerTask
import cn.sunline.saas.scheduler.job.component.CalculateSchedulerTimer
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

/**
 * @title: JobCommandRunner
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/31 14:59
 */
@Configuration
class JobCommandRunner(
    private val seq: Sequence,
    private val calculateSchedulerTimer: CalculateSchedulerTimer,
    private val loanAgreementService: LoanAgreementService,
    private val invoiceService: InvoiceService,
    private val schedulerJobLogService: SchedulerJobLogService,
    private val tenantDateTime: TenantDateTime,
    private val consumerLoanService: ConsumerLoanService,
    private val loanAccountService: LoanAccountService,
) {
    @PostConstruct
     fun run() {
        val invoiceAccountJob = InvoiceAccountJob(tenantDateTime, invoiceService, schedulerJobLogService, calculateSchedulerTimer, loanAgreementService)
        val loanAutoRepaymentJob = LoanAutoRepaymentJob(tenantDateTime, invoiceService, schedulerJobLogService, consumerLoanService)
        val loanInvoiceJob = LoanInvoiceJob(tenantDateTime, schedulerJobLogService, calculateSchedulerTimer, invoiceService, loanAccountService, loanAgreementService)
        ActorContext.registerActor("LoanAgreementSchedulerTask",LoanAgreementSchedulerTask(seq, calculateSchedulerTimer, loanAgreementService, invoiceService, invoiceAccountJob, loanAutoRepaymentJob, loanInvoiceJob, schedulerJobLogService, tenantDateTime))
        ActorContext.registerActor("InvoiceAccountActor",invoiceAccountJob)
        ActorContext.registerActor("LoanAutoRepaymentJob",loanAutoRepaymentJob)
        ActorContext.registerActor("LoanInvoiceJob",loanInvoiceJob)
    }
}
