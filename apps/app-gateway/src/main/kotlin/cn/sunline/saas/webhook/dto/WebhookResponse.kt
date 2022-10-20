package cn.sunline.saas.webhook.dto

data class WebhookResponse(val success: Boolean, val data: Map<String, String> = mapOf())
