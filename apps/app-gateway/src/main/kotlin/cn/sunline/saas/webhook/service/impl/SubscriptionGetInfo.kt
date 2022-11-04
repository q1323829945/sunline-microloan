package cn.sunline.saas.webhook.service.impl

import cn.sunline.saas.service.InstanceService
import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import cn.sunline.saas.webhook.dto.WebhookResponse
import cn.sunline.saas.webhook.service.Subscription
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class SubscriptionGetInfo(
    private val instanceService: InstanceService
): Subscription {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun run(dtoWebhookRequest: DTOWebhookRequest): WebhookResponse {
        val instance = instanceService.findByTenant(dtoWebhookRequest.tenant)
        instance?: run {
            return WebhookResponse(
                false,
                mutableMapOf(
                    "msg" to "tenant is not found!!!",
                )
            )
        }

        val gatewayApi = "Gateway"
        val apiMap = objectMapper.writeValueAsString(
            mapOf(
                "endpoint" to "https://quickloan-gateway.saas.finline.app",
                "access_key" to instance.accessKey,
                "client_id" to instance.id
            )
        )

        val microloanApiDoc = "Microloan_Api_Doc"
        val microloanApiDocMap = objectMapper.writeValueAsString(
            mapOf(
                "url" to "https://quickloan-gateway.saas.finline.app/doc?type=Microloan",
                "username" to "nothing",
                "password" to "nothing"
            )
        )


        return WebhookResponse(
            true,
            mutableMapOf(
                "SUBSCRIPTION_META_KEY_1" to gatewayApi,
                "${gatewayApi}_DESCRIPTION" to "General user manual for getting started",
                "${gatewayApi}_TYPE" to "api",
                "${gatewayApi}_NAME" to "API Gateway",
                "${gatewayApi}_VALUE" to apiMap,


                "SUBSCRIPTION_META_KEY_2" to microloanApiDoc,
                "${microloanApiDoc}_DESCRIPTION" to "Here you will find developer and interface documentation",
                "${microloanApiDoc}_TYPE" to "api_doc",
                "${microloanApiDoc}_NAME" to "API Document",
                "${microloanApiDoc}_VALUE" to microloanApiDocMap,

            )
        )
    }
}