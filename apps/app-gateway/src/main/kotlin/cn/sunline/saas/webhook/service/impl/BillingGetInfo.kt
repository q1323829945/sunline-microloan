package cn.sunline.saas.webhook.service.impl

import cn.sunline.saas.service.InstanceService
import cn.sunline.saas.service.StatisticsService
import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import cn.sunline.saas.webhook.dto.WebhookResponse
import cn.sunline.saas.webhook.service.Subscription
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class BillingGetInfo(
    private val instanceService: InstanceService,
    private val statisticsService: StatisticsService
): Subscription {
    override fun run(dtoWebhookRequest: DTOWebhookRequest): WebhookResponse {
        val instance = instanceService.findByTenant(dtoWebhookRequest.tenant)?:run{
            return WebhookResponse(
                false,
                mutableMapOf(
                    "msg" to "tenant is not found!!!",
                )
            )
        }

        val year = dtoWebhookRequest.data["BILLING_CYCLE_YEAR_KEY"]?.run { this.toInt() }
        val month = dtoWebhookRequest.data["BILLING_CYCLE_MONTH_KEY"]?.run { this.toInt() }
        val day = dtoWebhookRequest.data["BILLING_CYCLE_DAY_KEY"]?.run { this.toInt() }

        if(year != null && month != null && day != null){
            val count = statisticsService.findCountByDate(instance.tenant,year, month, day)

            return WebhookResponse(
                true,
                mutableMapOf(
                    "BILLING_API_VOLUME_KEY" to "$count",
                )
        )
        }

        return WebhookResponse(
            true
        )
    }

}