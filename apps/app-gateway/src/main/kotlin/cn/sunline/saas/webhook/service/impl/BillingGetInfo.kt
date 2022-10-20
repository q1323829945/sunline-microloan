package cn.sunline.saas.webhook.service.impl

import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import cn.sunline.saas.webhook.dto.WebhookResponse
import cn.sunline.saas.webhook.service.Subscription
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class BillingGetInfo(
): Subscription {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun run(dtoWebhookRequest: DTOWebhookRequest): WebhookResponse {
        return WebhookResponse(
            true
        )

//        return WebhookResponse(
//            true,
//            mutableMapOf(
//                "BILLING_API_VOLUME_KEY" to "$api",
//                "BILLING_USER_VOLUME_KEY" to "$customer",
//                "BILLING_TRANSACTION_VOLUME_KEY" to "$business"
//            )
//        )
    }

}