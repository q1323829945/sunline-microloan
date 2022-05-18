package cn.sunline.saas.dapr_wrapper.actor

import cn.sunline.saas.dapr_wrapper.actor.model.ActorContext
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor

/**
 * @title: TestRegisterActor
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/17 12:11
 */
class Test2RegisterActor(
    private val actorType: String = "Test2RegisteredActor",
    private val entityConfig: EntityConfig? = null
) :
    AbstractActor(actorType, entityConfig) {

    override fun doJob(actorId: String) {
        println("do my job")
    }
}
