package cn.sunline.saas.huaweicloud.apig.constant

data class EnvironmentCreateParams(
    val name:String,
    val remark:String? = null
)

data class EnvironmentUpdateParams(
    val id:String,
    val name:String,
    val remark:String? = null
)