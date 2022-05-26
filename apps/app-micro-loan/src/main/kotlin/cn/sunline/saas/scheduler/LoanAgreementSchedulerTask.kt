package cn.sunline.saas.scheduler

import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.invoice.service.InvoiceService
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import cn.sunline.saas.scheduler.job.component.CalculateSchedulerTimer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

/**
 * @title: LoanAgreementSchedulerTask
 * @description: choreography loan agreement timing jobs
 *               this task be called by reminder
 * @author Kevin-Cui
 * @date 2022/5/25 11:23
 */
@Component
class LoanAgreementSchedulerTask(
    actorType: String = "LoanAgreementSchedulerTask",
    order: Int = 0,
    entityConfig: EntityConfig? = null
) :
    AbstractActor(actorType, order, entityConfig) {

    @Autowired
    private lateinit var calculateSchedulerTimer: CalculateSchedulerTimer

    @Autowired
    private lateinit var loanAgreementService: LoanAgreementService

    @Autowired
    private lateinit var invoiceService: InvoiceService

    override fun doJob(actorId: String) {
        val baseDateTime = calculateSchedulerTimer.baseDateTime()

        val agreementId = actorId.toLong()

        val agreementAggregate = loanAgreementService.retrieve(agreementId)

        val invoices = invoiceService.listInvoiceByAgreementId(agreementId, Pageable.unpaged()).toList().sortBy { it.invoicePeriodToDate }

    }
}