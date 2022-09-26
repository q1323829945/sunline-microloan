package cn.sunline.saas.webhook.service.impl

import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import cn.sunline.saas.webhook.dto.WebhookResponse
import cn.sunline.saas.webhook.service.Subscription
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*

class SubscriptionGetInfo(
    private val tenantService: TenantService
): Subscription {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun run(dtoWebhookRequest: DTOWebhookRequest): WebhookResponse {
        val tenant = tenantService.findByUUID(UUID.fromString(dtoWebhookRequest.tenant))
        tenant?: run {
            return WebhookResponse(
                false,
                mutableMapOf(
                    "msg" to "tenant is not found!!!",
                )
            )
        }
        val api = "API"
        val apiDoc = "API_DOC"
        val management = "MANAGEMENT"
        val managementMap = objectMapper.writeValueAsString(mutableMapOf(
            "url" to "https://quickloan-management-demo.saas.finline.app",
            "username" to "admin",
            "password" to "admin",
            "client_id" to tenant.uuid.toString()
        ))
        val web = "WEB"
        val webMap = objectMapper.writeValueAsString(mutableMapOf(
            "url" to "https://quickloan-app-demo.saas.finline.app?tenant=${tenant.uuid}",
            "access_key" to "300348",
            "client_id" to "nothing",
        ))
        return WebhookResponse(
            true,
            mutableMapOf(
                "SUBSCRIPTION_META_KEY_1" to management,
                "${management}_DESCRIPTION" to "Access backend management portal",
                "${management}_TYPE" to "management",
                "${management}_NAME" to "Microloan Management Portal",
                "${management}_VALUE" to managementMap,

                "SUBSCRIPTION_META_KEY_2" to web,
                "${web}_DESCRIPTION" to "Integrate using client mobile web",
                "${web}_TYPE" to "client_web",
                "${web}_NAME" to "Microloan Mobile Web",
                "${web}_VALUE" to webMap,
            )
        )
    }
}