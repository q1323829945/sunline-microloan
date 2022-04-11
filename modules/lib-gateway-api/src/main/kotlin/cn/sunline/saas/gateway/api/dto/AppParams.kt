package cn.sunline.saas.gateway.api.dto

data class AppCreateParams(
    val name:String,
    val remark:String? = null,
    val appKey:String? = null,
    val appSecret:String? = null
)

data class AppUpdateParams(
    val id:String,
    val name:String,
    val remark:String? = null,
    val appKey:String? = null,
    val appSecret:String? = null
)

data class AuthsParams(
    val apiIds:List<String>,
    val appIds:List<String>,
    val envId:String
)

data class AppResponseParams(
    val id:String,
    val name:String,
    val appKey:String? = null,
    val appSecret:String? = null
)

data class AppAuthsResponseParams(
    val id:String,
    val apiId:String,
    val appId:String,
    val authResult:String
)



data class AppPagedParams(
    val id:String? = null,
    val name:String? = null,
    val pageNo:Int? = null,
    val pageSize:Int? = null,
)
