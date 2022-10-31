package cn.sunline.saas.dapr_wrapper.actor.model

import cn.sunline.saas.dapr_wrapper.actor.ActorCommand

/**
 * @title: RegisteredActor
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/17 10:42
 */
data class EntityConfig(
    val actorIdleTimeout: String? = null,
    val drainOngoingCallTimeout: String? = null,
    val reentrancyEnabled: Boolean? = null
)

abstract class AbstractActor(val actorType: String, val entityConfig: EntityConfig? = null) {
    abstract fun doJob(actorId: String, jobId: String,data: ActorCommand = ActorCommand())
}