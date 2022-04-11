package cn.sunline.saas.huaweicloud.apig.constant

data class HuaweiCloudAppCreateParams(
    val name:String,
    val remark:String? = null,
    val app_key:String? = null,
    val app_secret:String? = null
)

data class HuaweiCloudAppUpdateParams(
    val id:String,
    val name:String,
    val remark:String? = null,
    val app_key:String? = null,
    val app_secret:String? = null
)

data class HuaweiCloudAuthsParams(
    val api_ids:List<String>,
    val app_ids:List<String>,
    val env_id:String
)