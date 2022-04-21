package cn.sunline.saas.dapr_wrapper

data class BindingRequest(
    val data:Any,
    val metadata:Map<String,String>
)