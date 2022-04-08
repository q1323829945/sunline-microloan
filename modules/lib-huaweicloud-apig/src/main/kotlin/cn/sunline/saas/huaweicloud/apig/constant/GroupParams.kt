package cn.sunline.saas.huaweicloud.apig.constant

data class GroupCreateParams(
    val name:String,
    val remark:String? = null
)

data class GroupUpdateParams(
    val id:String,
    val name:String,
    val remark:String? = null
)