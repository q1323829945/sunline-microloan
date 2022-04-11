package cn.sunline.saas.rbac.dto


data class DTOUserChange(
    val email: String,
    val roleList: List<Long> = listOf()
)
