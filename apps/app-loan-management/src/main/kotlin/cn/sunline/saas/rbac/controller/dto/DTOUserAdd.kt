package cn.sunline.saas.rbac.controller.dto


data class DTOUserAdd(
    val username: String,
    val email: String,
    val password: String,
    val personId:String?,
)