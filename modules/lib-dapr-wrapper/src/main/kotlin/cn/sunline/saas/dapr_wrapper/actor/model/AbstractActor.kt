package cn.sunline.saas.dapr_wrapper.actor.model

import org.joda.time.DateTime

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
    fun registerActor() {
        ActorContext.registerActor(actorType, this)
    }

    abstract fun doJob(actorId: String, jobId: String)
}