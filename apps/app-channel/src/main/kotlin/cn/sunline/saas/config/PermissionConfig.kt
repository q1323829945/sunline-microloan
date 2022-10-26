package cn.sunline.saas.config

import org.springframework.web.bind.annotation.*

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
    ROLE_GET_ALL_LIST("role", "roles/all", RequestMethod.GET, "View role user list"),
    ROLE_ADD("role", "roles\$", RequestMethod.POST, "Add new role"),
    ROLE_MODIFY("role", "roles/[0-9]+\$", RequestMethod.PUT, "Modify role"),

    PERMISSION_GET("permission", "permissions/[0-9]+\$", RequestMethod.GET, "View permission profile"),
    PERMISSION_LIST("permission", "permissions(\\?.*|\$)", RequestMethod.GET, "View permission list"),
    PERMISSION_ALL_LIST("permission", "permissions/all", RequestMethod.GET, "View permission all list"),
    PERMISSION_ADD("permission", "permissions\$", RequestMethod.POST, "Add new permission"),
    PERMISSION_MODIFY("permission", "permissions/[0-9]+\$", RequestMethod.PUT, "Modify permission"),

    TENANT_GET_LIST("tenant", "tenants(\\?.*|\$)", RequestMethod.GET, "View tenant list"),
    TENANT_MODIFY("tenant", "tenants/[0-9]+\$", RequestMethod.PUT, "Modify tenant"),
    TENANT_STATUS_CHANGE("tenant", "tenants/[0-9]+/status", RequestMethod.PUT, "Modify tenant"),
    TENANT_DELETE("tenant", "tenants/[0-9]+\$", RequestMethod.DELETE, "Delete tenant"),

    RATE_PLAN_MODIFY("ratePlan", "RatePlan/[0-9]+\$", RequestMethod.PUT, "Modify rateplan"),
    RATE_PLAN_GET_LIST("ratePlan", "RatePlan(\\?.*|\$)", RequestMethod.GET, "View ratePlan list"),
    RATE_PLAN_GET_LIST_BY_TYPE("ratePlan", "RatePlan/all(\\?.*|\$)", RequestMethod.GET, "View ratePlan list by type"),
    RATE_PLAN_ADD("ratePlan", "RatePlan\$", RequestMethod.POST, "Add ratePlan role"),

    INTEREST_RATE_ADD("interestRate", "InterestRate\$", RequestMethod.POST, "Add interestRate"),
    INTEREST_RATE_GET_LIST("interestRate", "InterestRate(\\?.*|\$)", RequestMethod.GET, "View interestRate list"),
    INTEREST_RATE_MODIFY("interestRate", "InterestRate/[0-9]+\$", RequestMethod.PUT, "Modify interestRate"),
    INTEREST_RATE_DELETE("interestRate", "InterestRate/[0-9]+/[0-9]+\$", RequestMethod.DELETE, "Delete interestRate"),

    LOAN_APPLICATION_STATISTICS_GET_LIST("LoanApplicationStatistics","loanApplicationStatistics(\\?.*|\$)",RequestMethod.GET,"View loan application statistics"),
    LOAN_APPLICATION_DETAIL_ADD("LoanApplicationStatistics", "loanApplicationStatistics/addDetail\$", RequestMethod.POST, "Add loan application detail"),
    LOAN_APPLICATION_STATISTICS_ADD("LoanApplicationStatistics", "loanApplicationStatistics/addStatistics\$", RequestMethod.POST, "Add loan application statistics"),
    LOAN_APPLICATION_STATISTICS_GET_CHARTS("LoanApplicationStatistics", "loanApplicationStatistics/charts(\\?.*|\$)", RequestMethod.GET, "View loan application statistics charts"),

    COMMISSION_STATISTICS_GET_LIST("CommissionStatistics","commissionStatistics(\\?.*|\$)",RequestMethod.GET,"View commission statistics"),
    COMMISSION_DETAIL_ADD("CommissionStatistics", "commissionStatistics/addDetail\$", RequestMethod.POST, "Add commission detail"),
    COMMISSION_STATISTICS_ADD("CommissionStatistics", "commissionStatistics/addStatistics\$", RequestMethod.POST, "Add application statistics"),
    COMMISSION_STATISTICS_GET_CHARTS("CommissionStatistics", "commissionStatistics/charts(\\?.*|\$)", RequestMethod.GET, "View commission statistics charts"),

    CHANNEL_STATISTICS_GET_PIE("ChannelStatistics", "channelStatistics/pie(\\?.*|\$)", RequestMethod.GET, "View channel statistics pie"),


    PRODUCT_ADD("product", "product\$", RequestMethod.POST, "Add product"),
    PRODUCT_MODIFY("product", "product\$", RequestMethod.PUT, "Modify product"),
    PRODUCT_GET_LIST("product", "product/paged(\\?.*|\$)", RequestMethod.GET, "View product list"),
    PRODUCT_GET("product", "product/[0-9]+\$", RequestMethod.GET, "View product details"),

    //TODO:等之后加个问卷
    LOAN_APPLY_MODIFY("loan", "loan/update\$", RequestMethod.PUT, "Loan Apply supplement"),
    LOAN_APPLY_GET_LIST("loan", "loan(\\?.*|\$)", RequestMethod.GET, "View loan apply list"),
    LOAN_APPLY_GET("loan", "loan/[0-9]+\$", RequestMethod.GET, "View loan apply details"),
    LOAN_APPLY_SUBMIT("loan", "loan/submit\$", RequestMethod.PUT, "Loan apply submit"),
    LOAN_APPLY_APPROVAL("loan", "loan/approval\$", RequestMethod.PUT, "Loan apply approval"),
    LOAN_APPLY_GET_SUPPLEMENT_LIST("loan", "loan/supplement(\\?.*|\$)", RequestMethod.GET, "View loan apply supplement list"),
    LOAN_APPLY_SUPPLEMENT("loan", "loan/supplement\$", RequestMethod.PUT, "set loan apply supplement"),
    LOAN_APPLY_SET_PRODUCT("loan", "loan/product/set\$", RequestMethod.PUT, "set loan apply product"),
    LOAN_AGENT_GET("loan", "loan/agent/[0-9]+\$", RequestMethod.GET, "get loan agent"),
    LOAN_TEST1("loan", "loan/test1(\\?.*|\$)", RequestMethod.GET, "get loan agent"),
    LOAN_TEST2("loan", "loan/test2(\\?.*|\$)", RequestMethod.GET, "get loan agent"),
    LOAN_APPLY_STATUS_LIST("loan", "loan/applyStatus\$", RequestMethod.GET, " get applyStatus"),


    POSITION_GET("position", "position/[0-9A-Za-z]+\$", RequestMethod.GET, "View position profile"),
    POSITION_GET_LIST("position", "position(\\?.*|\$)", RequestMethod.GET, "View position list"),
    POSITION_ADD("position", "position\$", RequestMethod.POST, "Add new position"),
    POSITION_MODIFY("position", "position\$", RequestMethod.PUT, "Modify position"),

    CHANNEL_GET_LIST("channel", "channel(\\?.*|\$)", RequestMethod.GET, "channel get page list"),
    CHANNEL_GET_ALL_LIST("channel", "channel/all", RequestMethod.GET, "channel get all list"),
    CHANNEL_GET_DETAIL("channel", "channel/[0-9]+\$", RequestMethod.GET, "channel get detail"),
    CHANNEL_ADD("channel", "channel\$", RequestMethod.POST, "channel add"),
    CHANNEL_MODIFY("channel", "channel/[0-9]+\$", RequestMethod.PUT, "channel modify"),
    CHANNEL_ENABLE_MODIFY("channel", "channel/enable/[0-9]+\$", RequestMethod.PUT, "channel enable modify"),
    CHANNEL_AGREEMENT_LIST("channel", "channel/agreement/[0-9]+\$", RequestMethod.GET, "channel get agreement list"),
    CHANNEL_AGREEMENT_COMMISSION_ADD("channel", "channel/agreement/commission\$", RequestMethod.POST, "channel agreement add"),
    CHANNEL_AGREEMENT_COMMISSION_DETAIL("channel", "channel/agreement/commission/[0-9]+\$", RequestMethod.GET, "channel get agreement detail"),
    CHANNEL_AGREEMENT_TYPE_LIST("channel", "channel/agreement/agreementType\$", RequestMethod.GET, "channel get agreementType"),
    CHANNEL_CAST_TYPE_LIST("channel", "channel/channelCastType\$", RequestMethod.GET, "channel get castType"),

    //menu
    MENU_GET_LIST("menu","menus(\\?.*|\$)",RequestMethod.GET,"View menu"),
    USER_MENU("menu","userConfig", RequestMethod.GET,"View user menu"),
    ROLE_MENU("menu","roleConfig", RequestMethod.GET,"View role menu"),
    PERMISSION_MENU("menu","permissionConfig", RequestMethod.GET,"View permission menu"),
    RATE_PLAN_MENU("menu","ratePlanConfig", RequestMethod.GET,"View formal tenant menu"),
    COMMISSION_STATISTICS_QUERY_MENU("menu","commissionStatisticsConfig", RequestMethod.GET,"View commission statistics query menu"),
    LOAN_APPLICATION_STATISTICS_QUERY_MENU("menu","loanApplicationStatisticsConfig", RequestMethod.GET,"view loan application statistics query menu"),
    LOAN_PRODUCT_MENU("menu","loanProductConfig", RequestMethod.GET,"View product menu"),
    QUESTIONNAIRES_MENU("menu","questionnairesConfig", RequestMethod.GET,"View questionnaires menu"),
    LOAN_APPLY_SUPPLEMENT_MENU("menu","loanApplySupplementConfig", RequestMethod.GET,"View loan apply supplement menu"),
    LOAN_APPLY_QUERY_MENU("menu","loanApplyQueryConfig", RequestMethod.GET,"View loan apply query menu"),
    CHANNEL_QUERY_MENU("menu","channelQueryConfig", RequestMethod.GET,"View channel query menu"),
    POSITION_MENU("menu","positionConfig",RequestMethod.GET,"View position menu"),
    PDPA_MENU("menu","pdpaConfig", RequestMethod.GET,"View pdpaConfig menu"),
}

