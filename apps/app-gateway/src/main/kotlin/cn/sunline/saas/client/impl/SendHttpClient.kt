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

@Service
@Configuration
@ConditionalOnProperty(prefix = "gateway",name = ["send"], havingValue = "http")
class SendHttpClient: Client {
    override fun send(context: SendContext): ClientResponse? {
        var exception: Exception? = null
        val response = runBlocking(CoroutineName("${context.method}-${context.url}")) {
            logger.info { "Started RPC request: ${context.url}  ${context.method}" }
            try {
                val response = daprClient.request(context.url) {
                    context.headers?.forEach {
                        if(!ignoreHeader.contains(it.key)){
                            headers.append(it.key,it.value)
                        }
                    }
                    method = HttpMethod(context.method)
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

        logger.info { " Finished RPC request: ${context.url}  ${context.method}" }
        error(exception, context)

        return response
    }

    @OptIn(InternalAPI::class)
    override fun sendFormatData(context: SendContext): ClientResponse? {
        var exception: Exception?  = null
        val response = runBlocking(CoroutineName("${context.method}-${context.url}")){
            logger.info { "Started RPC formatData request: ${context.url} ${context.method}" }
            try {
                val response = daprClient.request(context.url) {
                    method = HttpMethod(context.method)
                    context.query?.forEach { (k, v) -> parameter(k, v) }
                    context.headers?.forEach {
                        if(!ignoreHeader.contains(it.key)){
                            headers.append(it.key,it.value)
                        }
                    }
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

        logger.info { " Finished RPC formatData request: ${context.url}  ${context.method}" }
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
            logger.error { "RPC request has encountered low level error: $exception" }
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