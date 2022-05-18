package cn.sunline.saas.dapr_wrapper.actor

import cn.sunline.saas.dapr_wrapper.actor.model.ActorContext
import cn.sunline.saas.dapr_wrapper.actor.request.DTOEntitiesConfig
import cn.sunline.saas.dapr_wrapper.actor.request.DTOEntitiesConfigReentrancy
import cn.sunline.saas.dapr_wrapper.actor.request.DTOReentrancy
import cn.sunline.saas.dapr_wrapper.actor.request.DTORegisteredActor
import cn.sunline.saas.dapr_wrapper.config.DaprConfig
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * @title: RegisteredActor
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/17 9:59
 */
@RestController
@RequestMapping("/dapr")
class ActorUserService(private val daprConfig: DaprConfig) {

    @GetMapping("/config")
    fun registeredActor(): ResponseEntity<DTOResponseSuccess<DTORegisteredActor>> {
        val actorTypes = ActorContext.getActorTypes()
        val entitiesConfig = mutableListOf<DTOEntitiesConfig>()
        actorTypes.forEach {
            val actor = ActorContext.getActor(it)
            actor.entityConfig?.run {
                val dtoEntitiesConfigReentrancy = this.reentrancyEnabled?.run { DTOEntitiesConfigReentrancy(this) }
                entitiesConfig.add(
                    DTOEntitiesConfig(
                        mutableListOf(it),
                        this.actorIdleTimeout,
                        this.drainOngoingCallTimeout,
                        dtoEntitiesConfigReentrancy
                    )
                )
            }
        }

        val dtoRegisteredActor = DTORegisteredActor(
            actorTypes,
            daprConfig.actorIdleTimeout,
            daprConfig.actorScanInterval,
            daprConfig.drainOngoingCallTimeout,
            daprConfig.drainRebalancedActors,
            DTOReentrancy(daprConfig.reentrancyEnabled, daprConfig.reentrancyMaxStackDepth),
            entitiesConfig
        )
        return DTOResponseSuccess(dtoRegisteredActor).response()
    }

    @PutMapping("/actors/{actorType}/{actorId}/method/{methodName}")
    fun invokeActorMethod(
        @PathVariable(name = "actorType") actorType: String,
        @PathVariable(name = "actorId") actorId: String,
        @PathVariable(name = "methodName") methodName: String
    ): ResponseEntity<DTOResponseSuccess<Unit>> {
        ActorContext.getActor(actorType).doJob(actorId)

        return DTOResponseSuccess(Unit).response()
    }

    @PutMapping("/actors/{actorType}/{actorId}/method/remind/{reminderName}")
    fun invokeReminder(
        @PathVariable(name = "actorType") actorType: String,
        @PathVariable(name = "actorId") actorId: String,
        @PathVariable(name = "reminderName") reminderName: String
    ): ResponseEntity<DTOResponseSuccess<Unit>> {
        ActorContext.getActor(actorType).doJob(actorId)

        return DTOResponseSuccess(Unit).response()
    }

    @GetMapping("/healthz")
    fun healthCheck(): ResponseEntity<DTOResponseSuccess<Unit>> {
        return DTOResponseSuccess(Unit).response()
    }
}