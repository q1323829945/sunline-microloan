package cn.sunline.saas.dapr_wrapper.pubsub

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
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

/**
 * Utility service to invoke Dapr RPC calls
 */
class PubSubService {
    companion object {
        var logger = KotlinLogging.logger {}
        val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val pubSubClient: HttpClient = HttpClient(CIO) {
            engine {
                threadsCount = 8
                pipelining = true
            }
            expectSuccess = false

            HttpResponseValidator {
                validateResponse {
                    when (it.status.value) {
                        403 -> throw generateError(it, ManagementExceptionCode.DAPR_SUBPUB_FORBIDDEN_ERROR)
                        404 -> throw generateError(it, ManagementExceptionCode.DAPR_SUBPUB_NO_ROUTE_ERROR)
                        500 -> throw generateError(it, ManagementExceptionCode.DAPR_SUBPUB_DELIVERY_ERROR)
                    }
                }
            }
        }

        /**
         * Make a POST request for RPC invocation
         * @param[serviceName]  Name of the calling service
         * @param[methodName]   Name of the calling method
         * @param[data]         Data to be posted (optional, default to null)
         * @param[headerParams] Additional data to be used in header (optional, default to empty map)
         * @param[tenant]       Tenant identifier (optional, default to null)
         * @return              Return a mapped response data
         * @throws ManagementException
         *                      When an empty response is received, throws `ManagementExceptionCode.DAPR_INVOCATION_EMPTY_RESPONSE`. For downstream exceptions,
         *                      error code and error message are extracted and placed in the `error` block. For other Dapr specific errors,
         *                      `ManagementExceptionCode.DAPR_INVOCATION_POST_ERROR` is thrown.
         */
        inline fun publish(pubSubName: String, topic: String, payload: Any? = null, tenant: String? = null) {
            var exception: Exception?  = null
            val seq = UUID.randomUUID().toString()
            val requestUrl = "http://localhost:3500/v1.0/publish/$pubSubName/$topic"
            try {
                runBlocking(CoroutineName("PUBSUB-$pubSubName-$topic")) {
                    logger.info { "[$seq] Started PubSub request: $requestUrl" }
                    pubSubClient.request(requestUrl) {
                        method = HttpMethod.Post
                        headers {
                            append("X-Sequence", seq)
                            tenant?.run { append("X-Tenant-Id", tenant) }
                        }
                        contentType(ContentType.Application.Json)
                        accept(ContentType.Application.Json)
                        payload?.run { objectMapper.writeValueAsString(payload) }
                    }
                }
            } catch (ex: Exception) {
                exception = ex
            }

            if (exception == null) {
                logger.info { "[$seq] Finished PubSub request [$pubSubName] [$topic]: $requestUrl" }
            } else {
                if (exception !is ManagementException) {
                    exception = ManagementException(
                        ManagementExceptionCode.DAPR_INVOCATION_NETWORK_ERROR,
                        exception?.localizedMessage,
                        data = mapOf("pubsub" to pubSubName, "topic" to topic)
                    )
                }

                logger.error { "[$seq] PubsSub request [$pubSubName] [$topic] has failed: $requestUrl - $exception" }
            }
        }

        private fun generateError(httpResponse: HttpResponse, managementCode: ManagementExceptionCode): ManagementException {
            val components = httpResponse.request.url.encodedPath.substringAfter("/v1.0/publish/").split("/")
            val responseText = runBlocking { String(httpResponse.readBytes()) }
            val sequence = httpResponse.request.headers["X-Sequence"] ?:""

            logger.warn { "[$sequence] SubPub request has encountered an error: $managementCode" }

            return ManagementException(
                managementCode,
                data = mapOf(
                    "code" to managementCode.code,
                    "response" to responseText,
                    "pubsub" to components[0],
                    "topic" to components[1],
                    "statusCode" to httpResponse.status.value.toString(),
                    "requestMethod" to httpResponse.request.method.value
                )
            )
        }
    }
}