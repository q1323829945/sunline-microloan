package cn.sunline.saas.runner

import cn.sunline.saas.consumer_loan.job.InvoiceAccountJob
import cn.sunline.saas.consumer_loan.job.LoanAutoRepaymentJob
import cn.sunline.saas.consumer_loan.job.LoanInvoiceJob
import cn.sunline.saas.dapr_wrapper.actor.model.ActorContext
import cn.sunline.saas.scheduler.LoanAgreementSchedulerTask
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
class JobCommandRunner {
    @Autowired
    private lateinit var actorContext: ActorContext

    @Autowired
    private lateinit var loanAgreementSchedulerTask: LoanAgreementSchedulerTask

    @Autowired
    private lateinit var  invoiceAccountJob: InvoiceAccountJob

    @Autowired
    private lateinit var loanAutoRepaymentJob: LoanAutoRepaymentJob

    @Autowired
    private lateinit var loanInvoiceJob: LoanInvoiceJob

    @PostConstruct
     fun run() {
        actorContext.registerActor("LoanAgreementSchedulerTask",loanAgreementSchedulerTask)
        actorContext.registerActor("InvoiceAccountActor",invoiceAccountJob)
        actorContext.registerActor("LoanAutoRepaymentJob",loanAutoRepaymentJob)
        actorContext.registerActor("LoanInvoiceJob",loanInvoiceJob)
    }
}
