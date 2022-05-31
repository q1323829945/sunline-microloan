package cn.sunline.saas.dapr_wrapper.actor

import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig

/**
 * @title: TestRegisterActor
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/17 12:11
 */
class Test1RegisterActor(
    actorType: String = "Test1RegisteredActor",
    entityConfig: EntityConfig? = null
) :
    AbstractActor(actorType, entityConfig) {

    override fun doJob(actorId: String,jobId:String) {
        throw Exception("do my $actorId job")
    }
}
