package cn.sunline.saas.webhook.service.impl

import cn.sunline.saas.gateway.api.GatewayServer
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import cn.sunline.saas.webhook.dto.WebhookResponse
import cn.sunline.saas.webhook.service.Subscription
import java.util.*

class SubscriptionRemove(
    private val tenantService: TenantService,
    private val gatewayServer: GatewayServer
): Subscription {

    override fun run(dtoWebhookRequest: DTOWebhookRequest): WebhookResponse {
        val tenant = tenantService.findByUUID(UUID.fromString(dtoWebhookRequest.tenant))
        tenant?.run {
            this.enabled = false
            val updateOne = tenantService.save(tenant)


            val server = gatewayServer.getOne(tenant.uuid.toString(),"app-micro-loan")
            server?.run {
                gatewayServer.remove(this.id)
            }

            return WebhookResponse(
                true,
                mutableMapOf(
                    "tenant" to updateOne.uuid.toString(),
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