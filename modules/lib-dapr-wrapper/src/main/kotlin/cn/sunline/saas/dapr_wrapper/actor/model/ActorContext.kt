package cn.sunline.saas.dapr_wrapper.actor.model

/**
 * @title: ActorContext
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/17 12:03
 */
object ActorContext {

    private val context = ThreadLocal<MutableMap<String, AbstractActor>>()


    fun registerActor(actorType: String, actor: AbstractActor) {
        context.get()?.run {
            context.get()[actorType] = actor
        }?:run {
            context.set(mutableMapOf(actorType to actor))
        }
    }

    fun getActorTypes(): MutableSet<String> {
        return context.get().keys
    }

    fun getActor(actorType: String): AbstractActor {
        return context.get()[actorType]!!
    }

    fun getActors():List<AbstractActor>{
        return context.get().values.toList()
    }
}

