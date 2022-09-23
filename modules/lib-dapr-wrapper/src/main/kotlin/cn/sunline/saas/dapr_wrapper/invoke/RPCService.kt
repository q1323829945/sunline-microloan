package cn.sunline.saas.dapr_wrapper.invoke

import cn.sunline.saas.dapr_wrapper.invoke.request.RPCRequest
import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import java.util.*

/**
 * Utility service to invoke Dapr RPC calls
 */
class RPCService {
    // Internal representation for downstream exceptions
    private data class ErrorResponse(val code: Int, val message: String)
    private data class DaprErrorResponse(val errorCode: String, val message: String)

    companion object {
        var logger = KotlinLogging.logger {}
        val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val rpcClient: HttpClient = HttpClient(CIO) {
            engine {
                threadsCount = 8
            }
            expectSuccess = false

            HttpResponseValidator {
                validateResponse {
                    when (it.status.value) {
                        in 300..399 -> throw generateError(it, ManagementExceptionCode.DAPR_INVOCATION_REDIRECT_ERROR)
                        in 400..499 -> throw generateError(it, ManagementExceptionCode.DAPR_INVOCATION_REQUEST_ERROR)
                        in 500..599 -> throw generateError(it, ManagementExceptionCode.DAPR_INVOCATION_SERVER_ERROR)
                    }
                }
            }

        }

        inline fun <reified T> get(rpcRequest: RPCRequest): T? {
            return makeRequest<T>(
                HttpMethod.Get,
                rpcRequest.getModuleName(),
                rpcRequest.getMethodName(),
                rpcRequest.getQueryParams(),
                rpcRequest.getPayload(),
                rpcRequest.getHeaderParams(),
                rpcRequest.tenant)
        }

        inline fun <reified T> post(rpcRequest: RPCRequest): T? {
            return makeRequest<T>(
                HttpMethod.Post,
                rpcRequest.getModuleName(),
                rpcRequest.getMethodName(),
                rpcRequest.getQueryParams(),
                rpcRequest.getPayload(),
                rpcRequest.getHeaderParams(),
                rpcRequest.tenant)
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
        inline fun <reified T> post(serviceName: String, methodName: String, payload: Any?, queryParams: Map<String, String> = mapOf(), headerParams: Map<String, String> = mapOf(), tenant: String? = null): T? {
            return makeRequest<T>(HttpMethod.Post, serviceName, methodName, queryParams, payload, headerParams, tenant)
        }

        /**
         * Make a GET request for RPC invocation
         * @param[serviceName]  Name of the calling service
         * @param[methodName]   Name of the calling method
         * @param[queryParams]  Data to be used as query parameters (optional, default to empty map)
         * @param[headerParams] Additional data to be used in header (optional, default to empty map)
         * @param[tenant]       Tenant identifier (optional, default to null)
         * @return              Return a mapped response data
         * @throws ManagementException
         *                      When an empty response is received, throws `ManagementExceptionCode.DAPR_INVOCATION_EMPTY_RESPONSE`. For downstream exceptions,
         *                      error code and error message are extracted and placed in the `error` block. For other Dapr specific errors,
         *                      `ManagementExceptionCode.DAPR_INVOCATION_POST_ERROR` is thrown.
         */
        inline fun <reified T> get(serviceName: String, methodName: String, queryParams: Map<String, String> = mapOf(), headerParams: Map<String, String> = mapOf(), tenant: String? = null): T? {
            return makeRequest<T>(HttpMethod.Get, serviceName, methodName, queryParams, null, headerParams, tenant)
        }

        inline fun <reified T> makeRequest(httpMethod: HttpMethod, serviceName: String, methodName: String, queryParams: Map<String, String>? = mapOf(), payload: Any? = null, headerParams: Map<String, String> = mapOf(), tenant: String? = null): T? {
            var exception: Exception?  = null
            val seq = UUID.randomUUID().toString()
            val requestUrl = "http://localhost:3500/v1.0/invoke/$serviceName/method/$methodName"
            val resp = runBlocking(CoroutineName("RPC-${httpMethod.value}")) {
                logger.info { "[$seq] Started RPC request: $requestUrl" }
                try {
                    rpcClient.request(requestUrl) {
                        method = httpMethod
                        headers {
                            headerParams.forEach { (t, u) -> append(t, u) }
                            append("X-Sequence", seq)
                            tenant?.run { append("X-Tenant-Id", tenant) }
                        }
//                        if (httpMethod == HttpMethod.Post) {
//                            contentType(ContentType.Application.Json)
//                        }
                        accept(ContentType.Application.Json)
                        queryParams?.forEach { (t, u) -> parameter(t, u) }
                        payload?.run { setBody(objectMapper.writeValueAsString(payload)) }
                    }.bodyAsText()
                } catch (ex: Exception) {
                    exception = ex
                    ""
                }
            }
            logger.info { "[$seq] Finished RPC request: $requestUrl" }

            if (exception is ManagementException) throw exception as ManagementException

            exception?.run {
                logger.error { "[$seq] RPC request has encountered low level error: $exception" }
                throw ManagementException(
                    ManagementExceptionCode.DAPR_INVOCATION_NETWORK_ERROR,
                    this.localizedMessage,
                    data = mapOf(
                        "service" to serviceName,
                        "method" to methodName,
                        "requestMethod" to httpMethod.value
                    )
                )
            }
            return if (resp.isBlank()) null else objectMapper.readValue<T>(resp)
        }

        private fun generateError(httpResponse: HttpResponse, managementCode: ManagementExceptionCode): ManagementException {
            val components = httpResponse.request.url.encodedPath.substringAfter("/v1.0/invoke/").replace("/method/", ":").split(":")
            val responseText = runBlocking { String(httpResponse.readBytes()) }
            var errorMessage = ""
            var errorCode = ""
            val sequence = httpResponse.request.headers["X-Sequence"] ?:""

            if (responseText.contains("errorCode")) {
                val err = objectMapper.readValue<DaprErrorResponse>(responseText)
                errorMessage = err.message
                errorCode = err.errorCode
            } else {
                val err = objectMapper.readValue<ErrorResponse>(responseText)
                errorMessage = err.message
                errorCode = err.code.toString()
            }

            logger.warn { "[$sequence] RPC request has encountered an error: $errorCode - $errorMessage" }

            return ManagementException(
                managementCode,
                errorMessage,
                data = mapOf(
                    "code" to errorCode,
                    "response" to responseText,
                    "service" to components[0],
                    "method" to components[1],
                    "statusCode" to httpResponse.status.value.toString(),
                    "requestMethod" to httpResponse.request.method.value
                )
            )
        }
    }


}