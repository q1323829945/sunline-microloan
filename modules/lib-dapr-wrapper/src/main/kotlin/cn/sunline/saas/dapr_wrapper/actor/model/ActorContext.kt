package cn.sunline.saas.dapr_wrapper.actor.model

/**
 * @title: ActorContext
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/17 12:03
 */
object ActorContext {
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