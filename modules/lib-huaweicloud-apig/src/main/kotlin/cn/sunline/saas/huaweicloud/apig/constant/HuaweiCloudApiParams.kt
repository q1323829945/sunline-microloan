package cn.sunline.saas.huaweicloud.apig.constant

import cn.sunline.saas.gateway.api.constant.ReqMethodType

data class ApiParams(
    val group_id:String,
    val name:String,
    val type:Int, //1:open 2:private
    val version:String? = null,
    val req_protocol:ReqProtocolEnum, //HTTP or HTTPS or BOTH; default HTTPS
    val req_method:ReqMethodType, // GET POST PUT DELETE HEAD PATCH OPTIONS ANY
    val req_uri:String,
    val match_mode:MatchMode? = null, // SWA or NORMAL;default NORMAL
    val remark:String? = null,
    val auth_type:AuthType,  //NONE APP IAM AUTHORIZER
    val auth_opt:AuthOpt? = null,
    val authorizer_id:String? = null,
    val backend_type:BackendType, // HTTP FUNCTION MOCK
    val tag:String? = null,
    val tags:List<String>? = null,
    val cors:Boolean? = null, //default false
    val body_remark:String? = null,
    val result_normal_sample:String? = null,
    val result_failure_sample:String? = null,
    val response_id:String? = null,
    val backend_api:BackendApi? = null, //required when backend_type is HTTP
    val mock_info:MockInfo? = null, //required when backend_type is MOCK
    val func_info:FuncInfo? = null, //required when backend_type is FUNCTION
    val req_params:List<ReqParams>? = null,
    val backend_params:List<BackendParams>? = null,
    val policy_https:List<PolicyHttps>? = null,
    val policy_mocks:List<PolicyMocks>? = null,
    val policy_functions:List<PolicyFunctions>? = null,
)

data class BackendApi(
    val url_domain:String,
    val version:String? = null,
    val req_protocol:ReqProtocolEnum, //HTTP or HTTPS
    val req_method:ReqMethodType, // GET POST PUT DELETE HEAD PATCH OPTIONS ANY
    val req_uri:String,
    val timeout:Int, //1 - 60000
    val remark:String? = null,
    val vpc_status:Int? = null, //1:use VPC 2:not use VPC
    val vpc_info:VpcInfo? = null, //required when vpc_status is 1
    val authorizer_id: String? = null
)

data class VpcInfo(
    val vpc_id:String,
    val vpc_proxy_host:String? = null
)

data class MockInfo(
    val result_content:String? = null,
    val version:String? = null,
    val remark:String? = null
)

data class FuncInfo(
    val function_urn:String,
    val invocation_type:InvocationType, // async sync
    val timeout:Int, //1 - 60000
    val version:String? = null,
    val remark:String?
)

data class ReqParams(
    val name:String,
    val type:Type, //STRING NUMBER
    val location:Location, //PATH QUERY HEADER
    val default_value:String? = null,
    val sample_value:String? = null,
    val required:Int? = null, //1:yes 2:no  if location is PATH default 1 else default 2
    val valid_enable:Int? = null, //1: open check 2: not open check  default 2
    val remark:String? = null,
    val enumerations:String? = null,
    val min_num:Int? = null, //effective when type is NUMBER
    val max_num:Int? = null, //effective when type is NUMBER
    val min_size:Int? = null,
    val max_size:Int? = null
)

data class BackendParams(
    val name:String,
    val location:Location, //PATH QUERY HEADER
    val origin:Origin, // REQUEST CONSTANT SYSTEM
    val value:String,
    val remark:String
)

data class PolicyHttps(
    val name:String,
    val url_domain:String,
    val req_protocol:ReqProtocolEnum, //HTTP or HTTPS or BOTH; default HTTPS
    val req_method:ReqMethodType, // GET POST PUT DELETE HEAD PATCH OPTIONS ANY
    val req_uri:String,
    val timeout:Int, //1 - 60000
    val vpc_status:Int? = null, //1:use VPC 2:not use VPC
    val vpc_info:VpcInfo? = null, //required when vpc_status is 1
    val effect_mode:EffectMode, //ALL ANY
    val conditions:List<Conditions>,
    val backend_params:List<BackendParams>? = null
)

data class PolicyMocks(
    val name:String,
    val result_content:String,
    val effect_mode:EffectMode, //ALL ANY
    val conditions:List<Conditions>,
    val backend_params:List<BackendParams>? = null
)

data class PolicyFunctions(
    val name:String,
    val function_urn:String,
    val invocation_type:InvocationType, // async sync
    val timeout:Int, //1 - 60000
    val version:String? = null,
    val effect_mode:EffectMode, //ALL ANY
    val conditions:List<Conditions>,
    val backend_params:List<BackendParams>? = null
)

data class Conditions(
    val condition_origin:ConditionOrigin,// params source
    val condition_value:String,
    val condition_type:String? = null, // exact enum pattern ; required when condition_origin is params
    val req_param_name:String? = null //required when condition_origin is params
)

data class AuthOpt(
    val app_code_auth_type:AppCodeAuthTypeEnum? = null //DISABLE HEADER default DISABLE;effective when RegisterParams auth_type is APP
)

data class ApiResponsePage(
    val id:String,
    val name:String,
    val group_id:String,
    val run_env_id:String?,
    val req_uri:String
)

// enum
enum class Type{
    STRING,NUMBER
}

enum class Location{
    PATH,QUERY,HEADER
}

enum class Origin{
    REQUEST,CONSTANT,SYSTEM
}

enum class AuthType{
    NONE,APP,IAM,AUTHORIZER
}

enum class BackendType{
    HTTP ,FUNCTION,MOCK
}

enum class MatchMode{
    SWA,NORMAL
}

enum class InvocationType{
    async,sync
}

enum class EffectMode{
    ALL,ANY
}

enum class ConditionOrigin{
    params,source
}

enum class ReqProtocolEnum {
    HTTP,HTTPS,BOTH
}

enum class AppCodeAuthTypeEnum{
    DISABLE,HEADER
}