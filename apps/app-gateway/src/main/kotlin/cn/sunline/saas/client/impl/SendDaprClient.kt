package cn.sunline.saas.client.impl

import cn.sunline.saas.client.Client
import cn.sunline.saas.client.Client.Companion.daprClient
import cn.sunline.saas.client.Client.Companion.ignoreHeader
import cn.sunline.saas.client.Client.Companion.logger
import cn.sunline.saas.client.dto.ClientResponse
import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.modules.dto.SendContext
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.IOUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.nio.charset.Charset
import java.util.*

@Service
@Configuration
@ConditionalOnProperty(prefix = "gateway",name = ["send"], havingValue = "dapr")
class SendDaprClient:Client {
    override fun send(context: SendContext): ClientResponse? {
        var exception: Exception?  = null
        val seq = UUID.randomUUID().toString()
        val requestUrl = "http://localhost:3500/v1.0/invoke/${context.server}/method${context.path}"
        val response = runBlocking(CoroutineName("RPC-${context.method}")) {
            logger.info { "[$seq] Started RPC request: $requestUrl" }
            try {
                val response = daprClient.request(requestUrl) {
                    method = HttpMethod(context.method)
                    headers {
                        context.headers?.forEach {
                            if(!ignoreHeader.contains(it.key)){
                                append(it.key,it.value)
                            }
                        }
                    }
                    accept(ContentType.Application.Json)
                    contentType(ContentType.Application.Json)
                    context.query?.forEach { (k, v) -> parameter(k, v) }
                    context.body?.run { setBody(IOUtils.toString(this, Charset.defaultCharset())) }
                }
                setResponse(response)
            } catch (ex: Exception) {
                exception = ex
                null
            }
        }

        logger.info { " Finished RPC request:${context.server} ${context.path}  ${context.method}" }
        error(exception, context)

        return response
    }

    @OptIn(InternalAPI::class)
    override fun sendFormatData(context: SendContext): ClientResponse? {
        var exception: Exception?  = null
        val seq = UUID.randomUUID().toString()
        val requestUrl = "http://localhost:3500/v1.0/invoke/${context.server}/method${context.path}"
        val response = runBlocking(CoroutineName("RPC-${context.method}")) {
            logger.info { "[$seq] Started RPC formatData request: $requestUrl" }
            try {
                val response = daprClient.request(requestUrl) {
                    method = HttpMethod(context.method)
                    headers {
                        context.headers?.forEach {
                            if(!ignoreHeader.contains(it.key)){
                                append(it.key,it.value)
                            }
                        }
                    }
                    context.query?.forEach { (k, v) -> parameter(k, v) }

                    body = MultiPartFormDataContent(formData{
                        context.parts?.forEach { part ->
                            if(!part.submittedFileName.isNullOrEmpty()){
                                append(part.name,part.submittedFileName, ContentType.MultiPart.FormData,part.size){
                                    writePacket(ByteReadPacket(part.inputStream.readAllBytes()))
                                }
                            } else {
                                append(part.name,part.name, ContentType.Application.Json,part.size){
                                    writePacket(ByteReadPacket(part.inputStream.readAllBytes()))
                                }
                            }
                        }
                    })
                }
                setResponse(response)
            } catch (ex: Exception) {
                exception = ex
                null
            }
        }

        logger.info { " Finished RPC formatData request:${context.server} ${context.path}  ${context.method}" }
        error(exception, context)

        return response
    }

    private suspend fun setResponse(response: HttpResponse): ClientResponse {
        val headers = mutableMapOf<String,String>()
        response.headers.toMap().mapValues { it.value[0] }.forEach {
            headers[it.key] = it.value
        }

        return ClientResponse(
            body = response.bodyAsChannel().toInputStream(),
            headers = headers,
            status = response.status.value
        )
    }

    private fun error(exception: Exception?,context: SendContext){
        exception?.run {
            Client.logger.error { "RPC request has encountered low level error: $exception" }
            throw ManagementException(
                ManagementExceptionCode.GATEWAY_NETWORK_ERROR,
                this.localizedMessage,
                data = mapOf(
                    "requestMethod" to context.method
                )
            )
        }
    }
}