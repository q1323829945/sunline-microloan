package cn.sunline.saas.webhook.dto

data class DTOWebhookRequest(
    val tenant:String,
    val subscriptionId:Int,
    val data:Map<String,String> = emptyMap(),
    val tenantInfo:Map<String,String> = emptyMap(),
)

