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
    USER_MODIFY("user", "users/[0-9]+\$", RequestMethod.PUT, "Modify user"),

    ROLE_GET("role", "roles/[0-9]+\$", RequestMethod.GET, "View role profile"),
    ROLE_GET_LIST("role", "roles(\\?.*|\$)", RequestMethod.GET, "View role list"),
    ROLE_ADD("role", "roles\$", RequestMethod.POST, "Add new role"),
    ROLE_MODIFY("role", "roles/[0-9]+\$", RequestMethod.PUT, "Modify role"),

    PERMISSION_GET("permission", "permissions/[0-9]+\$", RequestMethod.GET, "View permission profile"),
    PERMISSION_LIST("permission", "permissions(\\?.*|\$)", RequestMethod.GET, "View permission list"),
    PERMISSION_ADD("permission", "permissions\$", RequestMethod.POST, "Add new permission"),
    PERMISSION_MODIFY("permission", "permissions/[0-9]+\$", RequestMethod.PUT, "Modify permission"),

    TENANT_GET_LIST("tenant", "tenants(\\?.*|\$)", RequestMethod.GET, "View tenant list"),
    TENANT_MODIFY("tenant", "tenants/[0-9]+\$", RequestMethod.PUT, "Modify tenant"),
    TENANT_STATUS_CHANGE("tenant", "tenants/[0-9]+/status", RequestMethod.PUT, "Modify tenant"),
    TENANT_DELETE("tenant", "tenants/[0-9]+\$", RequestMethod.DELETE, "Delete tenant"),

    RATE_PLAN_MODIFY("ratePlan", "RatePlan/[0-9]+\$", RequestMethod.PUT, "Modify rateplan"),
    RATE_PLAN_GET_LIST("ratePlan", "RatePlan(\\?.*|\$)", RequestMethod.GET, "View ratePlan list"),
    RATE_PLAN_ADD("ratePlan", "RatePlan\$", RequestMethod.POST, "Add ratePlan role"),

    INTEREST_RATE_ADD("interestRate", "InterestRate\$", RequestMethod.POST, "Add interestRate role"),
    INTEREST_RATE_GET_LIST("interestRate", "InterestRate(\\?.*|\$)", RequestMethod.GET, "View interestRate list"),
    INTEREST_RATE_MODIFY("interestRate", "InterestRate/[0-9]+\$", RequestMethod.PUT, "Modify interestRate"),
    INTEREST_RATE_DELETE("interestRate", "InterestRate/[0-9]+\$", RequestMethod.DELETE, "Delete interestRate"),

    MENU_GET_LIST("menu","menus(\\?.*|\$)",RequestMethod.GET,"View menu"),
    USER_MENU("menu","userConfig", RequestMethod.GET,"View user menu"),
    ROLE_MENU("menu","roleConfig", RequestMethod.GET,"View role menu"),
    PERMISSION_MENU("menu","permissionConfig", RequestMethod.GET,"View permission menu"),
    FORMAL_TENANT_MENU("menu","tenantConfig", RequestMethod.GET,"View formal tenant menu")


}