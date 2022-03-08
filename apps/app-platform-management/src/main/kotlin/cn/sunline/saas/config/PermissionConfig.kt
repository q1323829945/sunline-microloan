package cn.sunline.saas.config

import org.springframework.web.bind.annotation.RequestMethod

enum class PermissionConfig (
        val permissionGroup: String = "",
        val resource: String = "",
        val method: RequestMethod,
        val remark: String = ""
) {
    USER_GET("user", "users/[0-9]+\$", RequestMethod.GET, "View user profile"),
    USER_GET_LIST("user", "users(\\?.*|\$)", RequestMethod.GET, "View user list"),
    USER_ADD("user", "users\$", RequestMethod.POST, "Add new user"),
    USER_MODIFY("user", "users/[0-9]+\$", RequestMethod.POST, "Modify user"),

    ROLE_GET("role", "roles/[0-9]+\$", RequestMethod.GET, "View role profile"),
    ROLE_GET_LIST("role", "roles(\\?.*|\$)", RequestMethod.GET, "View role list"),
    ROLE_ADD("role", "roles\$", RequestMethod.POST, "Add new role"),
    ROLE_MODIFY("role", "roles/[0-9]+\$", RequestMethod.POST, "Modify role"),

    PERMISSION_GET("permission", "permissions/[0-9]+\$", RequestMethod.GET, "View permission profile"),
    PERMISSION_LIST("permission", "permissions(\\?.*|\$)", RequestMethod.GET, "View permission list"),
    PERMISSION_ADD("permission", "permissions\$", RequestMethod.POST, "Add new permission"),
    PERMISSION_MODIFY("permission", "permissions/[0-9]+\$", RequestMethod.POST, "Modify permission"),

    TENANT_GET_LIST("tenant", "tenants(\\?.*|\$)", RequestMethod.GET, "View tenant list"),
    TENANT_MODIFY("tenant", "tenants/[0-9]+\$", RequestMethod.PUT, "Modify tenant"),
    TENANT_STATUS_CHANGE("tenant", "tenants/[0-9]+/status", RequestMethod.PUT, "Modify tenant"),
    TENANT_DELETE("tenant", "tenants/[0-9]+\$", RequestMethod.DELETE, "Delete tenant"),

    MENU_GET_LIST("menu","menus(\\?.*|\$)",RequestMethod.GET,"View menu"),
    USER_MENU("menu","userConfig", RequestMethod.GET,"View user menu"),
    ROLE_MENU("menu","roleConfig", RequestMethod.GET,"View role menu"),
    PERMISSION_MENU("menu","permissionConfig", RequestMethod.GET,"View permission menu"),
    FORMAL_TENANT_MENU("menu","tenantConfig", RequestMethod.GET,"View formal tenant menu")
}