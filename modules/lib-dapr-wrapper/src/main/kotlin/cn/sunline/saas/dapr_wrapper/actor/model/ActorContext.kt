package cn.sunline.saas.dapr_wrapper.actor.model

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @title: ActorContext
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/17 12:03
 */
@Component
class ActorContext {

    @Autowired
    private val context: MutableMap<String, AbstractActor> = mutableMapOf()

    fun registerActor(actorType: String, actor: AbstractActor) {
        context[actorType] = actor
    }

    fun getActorTypes(): MutableSet<String> {
        return context.keys
    }

    fun getActor(actorType: String): AbstractActor {
        return context[actorType]!!
    }

    fun getActors():MutableCollection<AbstractActor>{
        return context.values
    }
}