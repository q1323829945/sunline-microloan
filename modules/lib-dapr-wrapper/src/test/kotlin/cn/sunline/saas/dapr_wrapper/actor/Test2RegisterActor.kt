package cn.sunline.saas.dapr_wrapper.actor

import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig

/**
 * @title: TestRegisterActor
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/17 12:11
 */
class Test2RegisterActor(
    actorType: String = "Test2RegisteredActor",
    order:Int =2,
    entityConfig: EntityConfig? = null
) :
    AbstractActor(actorType,order, entityConfig) {

    override fun doJob(actorId: String) {
        println("do my job")
    }
}
