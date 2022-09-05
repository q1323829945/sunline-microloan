package cn.sunline.saas.channel.rbac.modules.dto

data class DTOCustomerView(
    val id:String,
    val username:String? = null,
    val userId:String,
)

data class DTOCustomerAdd(
    val username:String? = null,
    val userId:String,
)

data class DTOCustomerChange(
    val username:String? = null,
)