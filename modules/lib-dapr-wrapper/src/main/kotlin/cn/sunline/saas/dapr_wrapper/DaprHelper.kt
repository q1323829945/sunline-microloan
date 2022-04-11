package cn.sunline.saas.dapr_wrapper

import io.dapr.client.DaprClientBuilder
import io.dapr.client.domain.HttpExtension
import io.dapr.client.domain.Metadata

/**
 * @title: DaprClient
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/11 15:18
 */
object DaprHelper {

    private val client = DaprClientBuilder().build()

    fun <T> invoke(
        applId: String, methodName: String, request: Any?, httpExtension: HttpExtension, clazz: Class<T>
    ): T? {
        val metadata = mutableMapOf<String, String>()
        val result = client.invokeMethod(
            applId, methodName, request, httpExtension, metadata, clazz
        )
        return result.block()
    }

    fun publish(pubsubName: String, topicName: String, data: Any, ttlInSeconds: Long = 1000): Unit {
        val metadata = mutableMapOf<String, String>(Metadata.TTL_IN_SECONDS to ttlInSeconds.toString())

        client.publishEvent(
            pubsubName, topicName, data, metadata
        ).block()
    }

}