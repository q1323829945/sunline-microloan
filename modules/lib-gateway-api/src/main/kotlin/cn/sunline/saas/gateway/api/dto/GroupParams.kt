package cn.sunline.saas.gateway.api.dto

data class GroupCreateParams(
    val name:String,
    val remark:String? = null
)

data class GroupUpdateParams(
    val id:String,
    val name:String,
    val remark:String? = null
)

data class GroupResponseParams(
    val id:String,
    val name:String? = null,
)


data class GroupPagedParams(
    val id:String? = null,
    val name:String? = null,
    val pageNo:Int? = null,
    val pageSize:Int? = null,
)
