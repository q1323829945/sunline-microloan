package cn.sunline.saas.dapr_wrapper.actor.request

/**
 * @title: DTORegisteredActor
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/17 10:35
 */
data class DTORegisteredActor(
    val entities: MutableSet<String>,
    val actorIdleTimeout: String,
    val actorScanInterval: String,
    val drainOngoingCallTimeout: String,
    val drainRebalancedActors: Boolean,
    val reentrancy: DTOReentrancy,
    val entitiesConfig: MutableList<DTOEntitiesConfig>
)

data class DTOReentrancy(
    val enabled: Boolean,
    val maxStackDepth: Int
)

data class DTOEntitiesConfig(
    val entities: MutableList<String>,
    val actorIdleTimeout: String?,
    val drainOngoingCallTimeout: String?,
    val reentrancy: DTOEntitiesConfigReentrancy?
)

data class DTOEntitiesConfigReentrancy(
    val enabled: Boolean
)
