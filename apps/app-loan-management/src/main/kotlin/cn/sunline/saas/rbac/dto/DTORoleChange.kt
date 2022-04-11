package cn.sunline.saas.rbac.dto



data class DTORoleChange(
    val name: String,
    val remark: String,
    val permissionList: List<Long> = listOf()
)