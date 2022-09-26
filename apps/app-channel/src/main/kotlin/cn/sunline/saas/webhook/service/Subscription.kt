package cn.sunline.saas.webhook.service

import cn.sunline.saas.webhook.dto.DTOWebhookRequest
import cn.sunline.saas.webhook.dto.WebhookResponse

interface Subscription {
    fun run(dtoWebhookRequest: DTOWebhookRequest):WebhookResponse
}