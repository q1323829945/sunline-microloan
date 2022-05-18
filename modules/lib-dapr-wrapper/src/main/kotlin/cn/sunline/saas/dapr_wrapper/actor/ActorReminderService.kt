package cn.sunline.saas.dapr_wrapper.actor

import cn.sunline.saas.dapr_wrapper.actor.request.ReminderRequest
import cn.sunline.saas.dapr_wrapper.actor.request.Timer
import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging

/**
 * @title: ActorConfigure
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/11 16:50
 */
class ActorReminderService {

    companion object {
        private var logger = KotlinLogging.logger {}
        private val daprClient: HttpClient = HttpClient(CIO) {
            engine {
                threadsCount = 8
            }
            expectSuccess = false

            HttpResponseValidator {
                validateResponse {
                    when (it.status.value) {
                        500 -> throw generateError(it, ManagementExceptionCode.DAPR_ACTOR_REQUEST_FAILED)
                        400 -> throw generateError(
                            it,
                            ManagementExceptionCode.DAPR_ACTOR_NOT_FOUND_OR_MALFORMED_REQUEST
                        )
                    }
                }
            }
        }

        fun createReminders(
            actorType: String, actorId: String, name: String, dueTime: Timer, period: Timer?
        ) {
            val body = ReminderRequest(dueTime.toString(), period.toString())
            makeRequest(actorType, actorId, name, HttpMethod.Post, body)
        }

        fun deleteReminders(actorType: String, actorId: String, name: String) {
            makeRequest(actorType, actorId, name, HttpMethod.Delete, null)
        }

        private fun makeRequest(actorType: String, actorId: String, name: String, httpMethod: HttpMethod, body: Any?) {
            val requestUrl = "http://localhost:3500/v1.0/actors/$actorType/$actorId/reminders/$name"
            var exception: Exception? = null
            try {
                runBlocking(CoroutineName("ACTOR-Reminders-$actorType-$actorId-$name-[$httpMethod]")) {
                    logger.info { "[$actorId] Started Actor Reminders [$httpMethod] request: $requestUrl" }
                    daprClient.request(requestUrl) {
                        method = httpMethod
                        contentType(ContentType.Application.Json)
                        accept(ContentType.Application.Json)
                        setBody(body)
                    }
                }
            } catch (ex: Exception) {
                exception = ex
            }
            if (exception == null) {
                logger.info { "[$actorId] Finished Actor Reminders request [$actorType] [$actorId] [$name] [$httpMethod]: $requestUrl" }
            } else {
                if (exception !is ManagementException) {
                    exception = ManagementException(
                        ManagementExceptionCode.DAPR_ACTOR_NETWORK_ERROR,
                        exception.localizedMessage,
                        data = mapOf(
                            "actorType" to actorType,
                            "actorId" to actorId,
                            "name" to name,
                            "httpMethod" to httpMethod
                        )
                    )
                }
                logger.error { "[$actorId] Actor Reminders request [$actorType] [$actorId] [$name] [$httpMethod] has failed: $requestUrl - $exception" }
            }
        }

        private fun generateError(
            httpResponse: HttpResponse,
            managementCode: ManagementExceptionCode
        ): ManagementException {
            val components = httpResponse.request.url.encodedPath.substringAfter("/v1.0/actors/").split("/")
            val responseText = runBlocking { String(httpResponse.readBytes()) }
            val actorId = components[1]
            logger.warn { "[$actorId] SubPub request has encountered an error: $managementCode" }

            return ManagementException(
                managementCode,
                data = mapOf(
                    "code" to managementCode.code,
                    "response" to responseText,
                    "actorType" to components[0],
                    "actorId" to actorId,
                    "name" to components[3],
                    "statusCode" to httpResponse.status.value.toString(),
                    "requestMethod" to httpResponse.request.method.value
                )
            )
        }
    }
}