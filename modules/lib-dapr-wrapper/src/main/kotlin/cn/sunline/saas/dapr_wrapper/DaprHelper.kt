package cn.sunline.saas.dapr_wrapper

import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUserId
import io.dapr.client.DaprClient
import io.dapr.client.DaprClientBuilder
import io.dapr.client.domain.HttpExtension
import io.dapr.client.domain.Metadata
import mu.KotlinLogging

/**
 * @title: DaprClient
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/11 15:18
 */
object DaprHelper {

    private var logger = KotlinLogging.logger {}

    private var client: DaprClient

    init {
        System.setProperty("dapr.api.protocol","HTTP")
        client = DaprClientBuilder().build()
    }

    private val metadata =
        mutableMapOf<String, String>(
            Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
            Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
        )

    fun <T> invoke(
        applId: String, methodName: String, request: Any?, httpExtension: HttpExtension, clazz: Class<T>
    ): T? {
        logger.info("输入参数applId：$applId")
        logger.info("methodName：$methodName")
        logger.info("输入参数request：$request")
        logger.info("输入参数httpExtension：$httpExtension")
        logger.info("输入参数clazz：$clazz")
        val result = client.invokeMethod(
            applId, methodName, request, setHttpExtension(httpExtension), clazz
        )
        logger.info("输出参数result：$result")
        return result.block()
    }


    fun publish(pubsubName: String, topicName: String, data: Any, ttlInSeconds: Long = 1000): Unit {
        metadata[Metadata.TTL_IN_SECONDS] = ttlInSeconds.toString()
        client.publishEvent(
            pubsubName, topicName, setBindingRequest(data), metadata
        ).block()
    }


    fun binding(bindingName:String,bindingOperation:String,data: Any):Unit{
        client.invokeBinding(
            bindingName,bindingOperation,setBindingRequest(data), metadata,BindingRequest::class.java
        ).block()
    }

    private fun setBindingRequest(data: Any):BindingRequest{
        return BindingRequest(data, metadata)
    }


    private fun setHttpExtension(httpExtension: HttpExtension):HttpExtension{
        return HttpExtension(httpExtension.method,httpExtension.queryParams, metadata)
    }
}