package cn.sunline.saas.dapr_wrapper.actor.model

import cn.sunline.saas.dapr_wrapper.actor.model.ActorContext.deepCopy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

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
        return context[actorType]!!.deepCopy()
    }

    fun getActors():List<AbstractActor>{
        return context.values.map { it.deepCopy() }
    }

    private fun <T:Any>T.deepCopy():T{
        return this::class.primaryConstructor!!.let { primaryConstructor ->
            primaryConstructor.parameters.associateWith { params ->
                (this::class as KClass<T>).memberProperties.first {
                    it.isAccessible = true
                    it.name == params.name
                }.get(this)
            }.let {
                primaryConstructor.callBy(it)
            }
        }
    }
}

