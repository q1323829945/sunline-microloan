package cn.sunline.saas.webhook.service.impl

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.pdpa.services.PdpaAuthorityService
import cn.sunline.saas.runner.AppCommandRunner
import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import cn.sunline.saas.webhook.dto.WebhookResponse
import cn.sunline.saas.webhook.service.Subscription
import java.util.*

class SubscriptionAdd(
    private val tenantService: TenantService,
    private val appCommandRunner: AppCommandRunner
): Subscription {

    override fun run(dtoWebhookRequest: DTOWebhookRequest): WebhookResponse {
        val tenant = tenantService.findByUUID(UUID.fromString(dtoWebhookRequest.tenant))
        return if(tenant == null){
            addTenant(dtoWebhookRequest)
        } else {
            enableTenant(tenant)
        }
    }

    private fun addTenant(dtoWebhookRequest: DTOWebhookRequest):WebhookResponse{
        val country = dtoWebhookRequest.tenantInfo["country"]?.run {
            CountryType.values().first { it.countryName.contains(this) }
        }?: run {
            CountryType.CHN
        }

        val tenant = Tenant(
            name = dtoWebhookRequest.tenantInfo["entityName"]?: run { "default" },
            country = country,
            enabled = true,
            uuid = UUID.fromString(dtoWebhookRequest.tenant),
        )
        val saveOne = tenantService.save(tenant)

        ContextUtil.setTenant(saveOne.id.toString())
        appCommandRunner.run()

        return WebhookResponse(
            true,
            mutableMapOf(
                "tenant" to saveOne.uuid.toString(),
                "SUBSCRIPTION_STATUS_KEY" to "true"
            )
        )
    }

    private fun enableTenant(tenant: Tenant):WebhookResponse{
        tenant.enabled = true
        val updateOne = tenantService.save(tenant)

        return WebhookResponse(
            true,
            mutableMapOf(
                "tenant" to updateOne.uuid.toString(),
                "SUBSCRIPTION_STATUS_KEY" to "true"
            )
        )
    }
}