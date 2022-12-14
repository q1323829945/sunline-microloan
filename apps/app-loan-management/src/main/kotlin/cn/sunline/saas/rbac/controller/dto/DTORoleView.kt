package cn.sunline.saas.rbac.controller.dto

import cn.sunline.saas.rbac.controller.RoleController


data class DTORoleView(
    val id: String,
    val name: String,
    val remark: String,
    val permissions: List<DTOPermissionView>
)