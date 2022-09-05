package cn.sunline.saas.channel.rbac.modules.dto

import cn.sunline.saas.channel.rbac.modules.dto.DTOPermissionView

data class DTORoleView(
    val id: String,
    val name: String,
    val remark: String,
    val permissions: List<DTOPermissionView>
)

data class DTORoleChange(
    val name: String,
    val remark: String,
    val permissionList: List<Long> = listOf()
)