package cn.sunline.saas.dapr_wrapper.actor

import cn.sunline.saas.dapr_wrapper.actor.model.ActorContext
import cn.sunline.saas.dapr_wrapper.actor.request.DTOEntitiesConfig
import cn.sunline.saas.dapr_wrapper.actor.request.DTOEntitiesConfigReentrancy
import cn.sunline.saas.dapr_wrapper.actor.request.DTOReentrancy
import cn.sunline.saas.dapr_wrapper.actor.request.DTORegisteredActor
import cn.sunline.saas.dapr_wrapper.config.DaprConfig
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * @title: RegisteredActor
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/17 9:59
 */
@RestController
@RequestMapping
class ActorUserService(private val daprConfig: DaprConfig) {
    @Autowired
    private lateinit var actorContext: ActorContext


    @GetMapping("/dapr/config")
    fun registeredActor(): DTORegisteredActor {
        val actorTypes = actorContext.getActorTypes()
        val entitiesConfig = mutableListOf<DTOEntitiesConfig>()
        actorTypes.forEach {
            val actor = actorContext.getActor(it)
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

        return DTORegisteredActor(
            actorTypes,
            daprConfig.actorIdleTimeout,
            daprConfig.actorScanInterval,
            daprConfig.drainOngoingCallTimeout,
            daprConfig.drainRebalancedActors,
            DTOReentrancy(daprConfig.reentrancyEnabled, daprConfig.reentrancyMaxStackDepth),
            entitiesConfig
        )
    }

    @PutMapping("/actors/{actorType}/{actorId}/method/{methodName}")
    fun invokeActorMethod(
        @PathVariable(name = "actorType") actorType: String,
        @PathVariable(name = "actorId") actorId: String,
        @PathVariable(name = "methodName") methodName: String
    ): ResponseEntity<DTOResponseSuccess<Unit>> {
        actorContext.getActor(actorType).doJob(actorId,methodName)

        return DTOResponseSuccess(Unit).response()
    }

    @PutMapping("/actors/{actorType}/{actorId}/method/remind/{reminderName}")
    fun invokeReminder(
        @PathVariable(name = "actorType") actorType: String,
        @PathVariable(name = "actorId") actorId: String,
        @PathVariable(name = "reminderName") reminderName: String,
        @RequestBody data:ActorCommand): ResponseEntity<DTOResponseSuccess<Unit>> {
        actorContext.getActor(actorType).doJob(actorId,reminderName,data)
        return DTOResponseSuccess(Unit).response()
    }

    @PutMapping("/actors/{actorType}/{actorId}/method/timer/{timerName}")
    fun invokeTimer(
        @PathVariable(name = "actorType") actorType: String,
        @PathVariable(name = "actorId") actorId: String,
        @PathVariable(name = "timerName") timerName: String
    ): ResponseEntity<DTOResponseSuccess<Unit>> {
        actorContext.getActor(actorType).doJob(actorId,timerName)

        return DTOResponseSuccess(Unit).response()
    }

    @GetMapping("/healthz")
    fun healthCheck(): ResponseEntity<DTOResponseSuccess<Unit>> {
        return DTOResponseSuccess(Unit).response()
    }


    @DeleteMapping("/actors/{actorType}/{actorId}")
    fun deleteActor(
        @PathVariable(name = "actorType") actorType: String,
        @PathVariable(name = "actorId") actorId: String,
    ): ResponseEntity<DTOResponseSuccess<Unit>> {
        return DTOResponseSuccess(Unit).response()
    }
}