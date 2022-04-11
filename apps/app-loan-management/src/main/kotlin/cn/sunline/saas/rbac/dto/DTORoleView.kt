package cn.sunline.saas.rbac.dto

import cn.sunline.saas.rbac.controller.RoleController


data class DTORoleView(
    val id: Long,
    val name: String,
    val remark: String,
    val permissions: List<DTOPermissionView>
)