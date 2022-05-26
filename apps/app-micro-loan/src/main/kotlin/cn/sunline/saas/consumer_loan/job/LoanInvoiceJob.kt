



package cn.sunline.saas.consumer_loan.job

import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.db.Invoice

/**
 * @title: LoanInvoiceJob
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 14:52
 */
class LoanInvoiceJob(actorType: String = "LoanInvoiceJob",
                     entityConfig: EntityConfig? = null):AbstractActor(actorType,entityConfig) {
    override         TODO("Not yet implemented")
    }

    fun prerequisites(invoices: List<Invoice>): Boolean {
        invoices.filter {
            it.invoiceStatus == InvoiceStatus.ACCOUNTED
        }
        return invoices.isNotEmpty()
    }

}