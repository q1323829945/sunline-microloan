package cn.sunline.saas.webhook.service.impl

import cn.sunline.saas.service.GatewayService
import cn.sunline.saas.service.InstanceService
import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import cn.sunline.saas.webhook.dto.WebhookResponse
import cn.sunline.saas.webhook.service.Subscription

class SubscriptionRemove(
    private val instanceService: InstanceService,
    private val gatewayService: GatewayService
): Subscription {

    override fun run(dtoWebhookRequest: DTOWebhookRequest): WebhookResponse {
        val instance = instanceService.findByTenant(dtoWebhookRequest.tenant)
        instance?.run {
            instanceService.deleteInstance(this.id)

            gatewayService.remove(this.id)
            return WebhookResponse(
                true,
                mutableMapOf(
                    "SUBSCRIPTION_STATUS_KEY" to "true"
                )
            )
        }

        return WebhookResponse(
            false,
            mutableMapOf(
                "msg" to "tenant is not found!!!",
                "SUBSCRIPTION_STATUS_KEY" to "false"
            )
        )
    }
}