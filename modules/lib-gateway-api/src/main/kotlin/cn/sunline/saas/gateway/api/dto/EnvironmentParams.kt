package cn.sunline.saas.gateway.api.dto


data class EnvironmentCreateParams(
    val name:String,
    val remark:String? = null
)

data class EnvironmentUpdateParams(
    val id:String,
    val name:String,
    val remark:String? = null
)


data class EnvironmentResponseParams(
    val id:String,
    val name:String? = null,
)


data class EnvironmentPagedParams(
    val id:String? = null,
    val name:String? = null,
    val pageNo:Int? = null,
    val pageSize:Int? = null,
)

data class EnvironmentPagedResponseParams(
    val total:Int,
    val size:Int,
    val envs:List<EnvironmentResponseParams>
)
