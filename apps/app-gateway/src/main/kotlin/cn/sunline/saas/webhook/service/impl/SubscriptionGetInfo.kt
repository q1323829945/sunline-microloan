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
                "url" to "http://quickloan-gateway.saas.finline.app",
                "access_key" to instance.accessKey,
                "client_id" to instance.id
            )
        )


        return WebhookResponse(
            true,
            mutableMapOf(
                "SUBSCRIPTION_META_KEY_1" to gatewayApi,
                "${gatewayApi}_DESCRIPTION" to "General user manual for getting started",
                "${gatewayApi}_TYPE" to "api",
                "${gatewayApi}_NAME" to "MicroLoan Server Api",
                "${gatewayApi}_VALUE" to apiMap,

            )
        )
    }
}