package cn.sunline.saas.rbac.dto


data class DTOUserView(
    val id: Long,
    val username: String,
    val email: String,
    val roles: List<DTOUserRoleView>
)