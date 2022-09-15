package cn.sunline.saas.dapr_wrapper.bindings

import cn.sunline.saas.dapr_wrapper.bindings.request.BindingsBaseRequest
import cn.sunline.saas.dapr_wrapper.bindings.request.BindingsRequest
import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getPermissions
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.util.*


class BindingsService {
    companion object {

        private var logger = KotlinLogging.logger {}
        val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        private val bindingsClient: HttpClient = HttpClient(CIO) {
            engine {
                threadsCount = 8
                pipelining = true
            }
            expectSuccess = false
            HttpResponseValidator {
                validateResponse {
                    when (it.status.value) {
                        403 -> throw generateError(it, ManagementExceptionCode.DAPR_BINDINGS_FORBIDDEN_ERROR)
                        404 -> throw generateError(it, ManagementExceptionCode.DAPR_BINDINGS_NO_ROUTE_ERROR)
                        500 -> throw generateError(it, ManagementExceptionCode.DAPR_BINDINGS_DELIVERY_ERROR)
                    }
                }
            }
        }

        /**
         * Make a POST request for RPC invocation
         * @param[bindingsName]  Name of the calling bingdings service
         * @param[operation]     Name of the calling bingdings operation
         * @param[data]          Data to be posted (optional, default to null)
         * @param[headerParams]  Additional data to be used in header (optional, default to empty map)
         * @param[tenant]       Tenant identifier (optional, default to null)
         *
         * @return              Return a mapped response data
         * @throws ManagementException
         *                      When an empty response is received, throws `ManagementExceptionCode.DAPR_INVOCATION_EMPTY_RESPONSE`. For downstream exceptions,
         *                      error code and error message are extracted and placed in the `error` block. For other Dapr specific errors,
         *                      `ManagementExceptionCode.DAPR_INVOCATION_POST_ERROR` is thrown.
         */
        fun bindings(bindingsName: String,operation: String, payload: Any? = null, tenant: String? = null) {
//            if(checkPermissions(bindingsName)){
//                logger.info { "没有权限" }
//                return
//            }

            var exception: Exception?  = null
            val seq = UUID.randomUUID().toString()
            val requestUrl = "http://localhost:3500/v1.0/bindings/$bindingsName"
            try {
                runBlocking(CoroutineName("BINDINGS-$bindingsName-$operation")) {
                    logger.info { "[$seq] Started Bindings request: $requestUrl" }
                    bindingsClient.request(requestUrl) {
                        method = HttpMethod.Post
                        headers {
                            append("X-Sequence", seq)
                            tenant?.run { append(Header.TENANT_AUTHORIZATION.key, tenant) }
                        }
                        accept(ContentType.Application.Json)
                        payload?.run {
                            val content = objectMapper.writeValueAsString(BindingsBaseRequest(BindingsRequest(payload),operation))
                            logger.info{  "[$seq] Started Bindings request body: $content" }
                            setBody(content)
                        }
                    }
                }
            } catch (ex: Exception) {
                exception = ex
            }

            if (exception == null) {
                logger.info { "[$seq] Finished Bindings request [$bindingsName] [$operation]: $requestUrl" }
            } else {
                if (exception !is ManagementException) {
                    exception = ManagementException(
                        ManagementExceptionCode.DAPR_PUBSUB_NETWORK_ERROR,
                        exception.localizedMessage,
                        data = mapOf("bindings" to bindingsName, "operation" to operation)
                    )
                }

                logger.error { "[$seq] Bindings request [$bindingsName] [$operation] has failed: $requestUrl - $exception" }
            }
        }

        private fun generateError(httpResponse: HttpResponse, managementCode: ManagementExceptionCode): ManagementException {
            val components = httpResponse.request.url.encodedPath.substringAfter("/v1.0/bindings/").split("/")
            val responseText = runBlocking { String(httpResponse.readBytes()) }
            val sequence = httpResponse.request.headers["X-Sequence"] ?:""

            logger.warn { "[$sequence] bindings request has encountered an error: $managementCode" }

            return ManagementException(
                managementCode,
                data = mapOf(
                    "code" to managementCode.code,
                    "response" to responseText,
                    "bindings" to components[0],
                    "statusCode" to httpResponse.status.value.toString(),
                    "requestMethod" to httpResponse.request.method.value
                )
            )
        }

        private fun checkPermissions(applicationId:String):Boolean{
            val permissions = ContextUtil.getPermissions()
            if(permissions.isNullOrEmpty()){
                logger.info { "permissions is empty" }
            }
            permissions?.firstOrNull { applicationId.startsWith(it) }?.run {
                return false
            }?: return true
        }
    }
}