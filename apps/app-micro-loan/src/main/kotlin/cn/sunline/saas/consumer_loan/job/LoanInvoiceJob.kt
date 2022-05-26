package cn.sunline.saas.consumer_loan.job

import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig

/**
 * @title: LoanInvoiceJob
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 14:52
 */
class LoanInvoiceJob(actorType: String = "LoanInvoiceJob",
                     order: Int = 2,
                     entityConfig: EntityConfig? = null):AbstractActor(actorType,order,entityConfig) {
    override fun doJob(actorId: String) {
        TODO("Not yet implemented")
    }

}