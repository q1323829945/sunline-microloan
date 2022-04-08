package cn.sunline.saas.huaweicloud.apig.constant

data class AppCreateParams(
    val name:String,
    val remark:String? = null,
    val app_key:String? = null,
    val app_secret:String? = null
)

data class AppUpdateParams(
    val id:String,
    val name:String,
    val remark:String? = null,
    val app_key:String? = null,
    val app_secret:String? = null
)

data class AuthsParams(
    val api_ids:List<String>,
    val app_ids:List<String>,
    val env_id:String
)

data class AppResponseParams(
    val id:String,
    val name:String,
    val app_key:String,
    val app_secret:String,
)

data class AppAuthsResponseParams(
    val id:String,
    val api_id:String,
    val app_id:String,
    val auth_result:String
)