package cn.sunline.saas.webhook.service.impl

import cn.sunline.saas.modules.dto.DTOInstance
import cn.sunline.saas.service.GatewayService
import cn.sunline.saas.service.InstanceService
import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import cn.sunline.saas.webhook.dto.WebhookResponse
import cn.sunline.saas.webhook.service.Subscription

class SubscriptionAdd(
    private val instanceService: InstanceService,
    private val gatewayService: GatewayService
): Subscription {

    override fun run(dtoWebhookRequest: DTOWebhookRequest): WebhookResponse {
        val instance = instanceService.register(DTOInstance(dtoWebhookRequest.tenant))
        gatewayService.addOrUpdate(instance.id)
        return WebhookResponse(
            true,
            mutableMapOf(
                "client_id" to instance.id,
                "access_key" to instance.accessKey,
                "tenant" to instance.tenant,
                "SUBSCRIPTION_STATUS_KEY" to "true"
            )
        )
    }
}