package cn.sunline.saas.consumer_loan.job

import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig

/**
 * @title: LoanAutoRepaymentJob
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 14:44
 */
class LoanAutoRepaymentJob(
    actorType: String = "LoanAutoRepaymentJob",
    entityConfig: EntityConfig? = null
) :
    AbstractActor(actorType, entityConfig) {

    override fun doJob(actorId: String) {
        TODO()
    }
}