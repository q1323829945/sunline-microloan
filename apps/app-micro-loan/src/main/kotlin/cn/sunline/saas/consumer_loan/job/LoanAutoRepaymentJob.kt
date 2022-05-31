package cn.sunline.saas.consumer_loan.job

import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.db.Invoice
import cn.sunline.saas.invoice.model.dto.RepaymentStatus
import org.springframework.stereotype.Service

/**
 * @title: LoanAutoRepaymentJob
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 14:44
 */
@Service
class LoanAutoRepaymentJob(

    actorType: String = "LoanAutoRepaymentJob",
    entityConfig: EntityConfig? = null
) :
    AbstractActor(actorType, entityConfig) {

    fun prerequisites(invoices: List<Invoice>): Boolean {
        invoices.filter {
            it.invoiceStatus == InvoiceStatus.ACCOUNTED && (it.repaymentStatus == RepaymentStatus.UNDO || it.repaymentStatus == RepaymentStatus.OVERDUE)
        }
        return invoices.isNotEmpty()
    }

    override fun doJob(actorId: String, jobId: String) {
        TODO()
    }

}