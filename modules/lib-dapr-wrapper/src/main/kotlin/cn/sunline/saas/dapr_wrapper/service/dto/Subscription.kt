package cn.sunline.saas.dapr_wrapper.service.dto

data class Subscription(
    val pubsubname:String,
    val topic:String,
    val route:String
)