package cn.sunline.saas.client

import cn.sunline.saas.client.dto.ClientResponse
import cn.sunline.saas.modules.dto.SendContext
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import mu.KotlinLogging


interface Client {
    companion object {
        @JvmStatic
        val daprClient: HttpClient = HttpClient(CIO) {
            engine {
                threadsCount = 8
            }
            expectSuccess = false
        }
        @JvmStatic
        val ignoreHeader = mutableListOf(
            "connection","content-length","host","content-type","client_id","access_key"
        )
        @JvmStatic
        var logger = KotlinLogging.logger {}
    }

    fun send(context: SendContext): ClientResponse?

    fun sendFormatData(context: SendContext): ClientResponse?
}