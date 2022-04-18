package cn.sunline.saas.dapr_wrapper

import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getRequestId
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUserId
import io.dapr.client.DaprClientBuilder
import io.dapr.client.domain.HttpExtension
import io.dapr.client.domain.InvokeMethodRequest
import io.dapr.client.domain.Metadata

/**
 * @title: DaprClient
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/11 15:18
 */
object DaprHelper {

    private val client = DaprClientBuilder().build()

    private val metadata =
        mutableMapOf<String, String>(
            Header.TENANT_AUTHORIZATION.name to ContextUtil.getTenant().toString(),
            Header.REQUEST_ID.name to ContextUtil.getRequestId(),
            Header.USER_AUTHORIZATION.name to ContextUtil.getUserId()
        )

    fun <T> invoke(
        applId: String, methodName: String, request: Any?, httpExtension: HttpExtension, clazz: Class<T>
    ): T? {

        val result = client.invokeMethod(
            applId, methodName, request, setHttpExtension(httpExtension), clazz
        )

        return result.block()
    }


    fun publish(pubsubName: String, topicName: String, data: Any, ttlInSeconds: Long = 1000): Unit {
        metadata[Metadata.TTL_IN_SECONDS] = ttlInSeconds.toString()
        client.publishEvent(
            pubsubName, topicName, data, metadata
        ).block()
    }


    private fun setHttpExtension(httpExtension: HttpExtension):HttpExtension{
        return HttpExtension(httpExtension.method,httpExtension.queryParams, metadata)
    }
}