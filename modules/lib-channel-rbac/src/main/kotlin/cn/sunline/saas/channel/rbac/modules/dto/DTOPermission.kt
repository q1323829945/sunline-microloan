package cn.sunline.saas.channel.rbac.modules.dto

data class DTOPermissionView(
    val id:String,
    val name: String,
    val remark: String
)


data class DTOPermission(val id: String, val tag: String, val name: String, val remark: String)