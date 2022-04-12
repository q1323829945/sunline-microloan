package cn.sunline.saas.gateway.api.dto

import cn.sunline.saas.gateway.api.constant.ActionType
import cn.sunline.saas.gateway.api.constant.AuthType
import cn.sunline.saas.gateway.api.constant.ReqMethodType

data class APiCreateParams(
    val apiName:String,
    val backendUri:String,
    val reqMethodType: ReqMethodType,
    val groupId:String,
    val domainUrl:String,
    val authType:AuthType? = null,
    val remark:String? = null
)


data class APiUpdateParams(
    val id:String,
    val apiName:String,
    val backendUri:String,
    val reqMethodType: ReqMethodType,
    val groupId:String,
    val domainUrl:String,
    val authType:AuthType? = null,
    val remark:String? = null
)

data class ApiResponseParams(
    val id:String,
    val apiName:String? = null,
    val groupId:String? = null,
    val envId:String? = null,
    val backendUri:String? = null,

)

data class BatchPublishParams(
    val action: ActionType, // online offline
    val apis:List<String>,
    val env_id:String,
    val remark:String? = null
)


data class OfflineParams(
    val api_id:String,
    val env_id:String,
)


data class OnlineParams(
    val api_id:String,
    val env_id:String,
    val remark:String? = null
)

data class ApiPagedParams(
    val id:String? = null,
    val name:String? = null,
    val pageNo:Int? = null,
    val pageSize:Int? = null,
    val groupId:String? = null,
    val evnId:String? = null
)


data class ApiPageResponseParams(
    val total: Int,
    val size: Int,
    val apis:List<ApiResponseParams>
)