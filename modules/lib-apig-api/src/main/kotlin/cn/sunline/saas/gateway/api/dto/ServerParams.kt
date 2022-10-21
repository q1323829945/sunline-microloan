package cn.sunline.saas.gateway.api.dto

data class ServerParams(
    val tenant:String,
    val domain:String,
    val server:String,
)

data class ServerView(
    val id:String,
    val instanceId:String,
    val server:String,
    val domain:String,
    var accessKey:String
)