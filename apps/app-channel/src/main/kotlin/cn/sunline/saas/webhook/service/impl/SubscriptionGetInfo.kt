package cn.sunline.saas.webhook.service.impl

import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.obs.api.GetParams
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import cn.sunline.saas.webhook.dto.WebhookResponse
import cn.sunline.saas.webhook.service.Subscription
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.*

class SubscriptionGetInfo(
    private val tenantService: TenantService,
    private val obsApi: ObsApi
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
        val agentManagement = "AGENT_MANAGEMENT"
        val agentManagementMap = objectMapper.writeValueAsString(mutableMapOf(
            "url" to "https://quickloan-agent.saas.finline.app",
            "username" to "admin",
            "password" to "admin",
            "client_id" to tenant.uuid.toString()
        ))
        val channelManagement = "CHANNEL_MANAGEMENT"
        val channelManagementMap = objectMapper.writeValueAsString(mutableMapOf(
            "url" to "https://agent-portal.saas.finline.app",
            "username" to "admin",
            "password" to "admin",
            "client_id" to "from 'Agent Management Portal'"
        ))

        val appServer = "APP_SERVER"
        val appServerMap = objectMapper.writeValueAsString(mutableMapOf(
            "url" to obsApi.getPictureView(GetParams("appServer.png")),
            "username" to "from 'Channel Management Portal'",
            "password" to "from 'Channel Management Portal'",
            "client_id" to "nothing"
        ))

        return WebhookResponse(
            true,
            mutableMapOf(
                "SUBSCRIPTION_META_KEY_1" to agentManagement,
                "${agentManagement}_DESCRIPTION" to "Access micro loan agent backend management portal",
                "${agentManagement}_TYPE" to "management",
                "${agentManagement}_NAME" to "Micro Loan Agent Management Portal",
                "${agentManagement}_VALUE" to agentManagementMap,

                "SUBSCRIPTION_META_KEY_2" to channelManagement,
                "${channelManagement}_DESCRIPTION" to "Access channel backend management portal",
                "${channelManagement}_TYPE" to "management",
                "${channelManagement}_NAME" to "Channel Management Portal",
                "${channelManagement}_VALUE" to channelManagementMap,

                "SUBSCRIPTION_META_KEY_3" to appServer,
                "${appServer}_DESCRIPTION" to "App server Download",
                "${appServer}_TYPE" to "management",
                "${appServer}_NAME" to "Channel App Server Download",
                "${appServer}_VALUE" to appServerMap,
            )
        )
    }
}