package cn.sunline.saas.rbac.controller.dto


data class DTOUserChange(
    val email: String,
    val roleList: List<Long> = listOf(),
    val personId:String?,
)
