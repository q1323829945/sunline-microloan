package cn.sunline.saas.gateway.api.dto

data class ServerParams(
    val tenant:String,
    val domain:String,
    val server:String,
)

data class ServerView(
    val id:String,
    val accessKey:String
)