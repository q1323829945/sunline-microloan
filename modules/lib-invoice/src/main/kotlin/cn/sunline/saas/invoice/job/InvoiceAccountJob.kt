package cn.sunline.saas.invoice.job

import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.service.InvoiceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: InvoiceAccountJob
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/23 16:25
 */
@Service
class InvoiceAccountJob(
    actorType: String = "InvoiceAccountActor",
    entityConfig: EntityConfig? = null
) : AbstractActor(actorType, entityConfig) {

    @Autowired
    private lateinit var invoiceService: InvoiceService

    /**
     *  get last invoice, then check its repayment status
     *  when it equaled to overdue then compute penalty interest and outstanding amount
     *  when it equaled to mini payment amount then compute outstanding amount
     *  change status to finished
     */
    override fun doJob(actorId: String) {
        val id = actorId as Long
        val invoice = invoiceService.getOne(id)

        //invoice.invoiceStatus = InvoiceStatus.ACCOUNTED

    }
}