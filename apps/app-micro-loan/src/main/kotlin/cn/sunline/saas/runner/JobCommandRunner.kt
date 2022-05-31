package cn.sunline.saas.runner

import cn.sunline.saas.consumer_loan.job.InvoiceAccountJob
import cn.sunline.saas.consumer_loan.job.LoanAutoRepaymentJob
import cn.sunline.saas.consumer_loan.job.LoanInvoiceJob
import cn.sunline.saas.scheduler.LoanAgreementSchedulerTask
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * @title: JobCommandRunner
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/31 14:59
 */
@Component
class JobCommandRunner : CommandLineRunner {
    override fun run(vararg args: String?) {
        LoanAgreementSchedulerTask().registerActor()
        InvoiceAccountJob().registerActor()
        LoanAutoRepaymentJob().registerActor()
        LoanInvoiceJob().registerActor()
    }
}
