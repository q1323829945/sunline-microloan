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

    DOCUMENT_TEMPLATE_ADD("documentTemplate", "DocumentTemplate\$", RequestMethod.POST, "Add documentTemplate"),
    DOCUMENT_TEMPLATE_GET_LIST("documentTemplate", "DocumentTemplate(\\?.*|\$)", RequestMethod.GET, "View documentTemplate list"),
    DOCUMENT_TEMPLATE_MODIFY("documentTemplate", "DocumentTemplate/[0-9]+\$", RequestMethod.POST, "Modify documentTemplate"),
    DOCUMENT_TEMPLATE_DELETE("documentTemplate", "DocumentTemplate/[0-9]+\$", RequestMethod.DELETE, "Delete documentTemplate"),
    DOCUMENT_TEMPLATE_DOWNLOAD("documentTemplate", "DocumentTemplate/download/[0-9]+\$", RequestMethod.GET, "download documentTemplate"),

    DOCUMENT_TEMPLATE_DIRECTORY_ADD("documentTemplateDirectory", "DocumentTemplateDirectory\$", RequestMethod.POST, "Add DocumentTemplateDirectory"),
    DOCUMENT_TEMPLATE_DIRECTORY_GET_LIST("documentTemplateDirectory", "DocumentTemplateDirectory(\\?.*|\$)", RequestMethod.GET, "View DocumentTemplateDirectory list"),
    DOCUMENT_TEMPLATE_DIRECTORY_MODIFY("documentTemplateDirectory", "DocumentTemplateDirectory/[0-9]+\$", RequestMethod.PUT, "Modify DocumentTemplateDirectory"),
    DOCUMENT_TEMPLATE_DIRECTORY_DELETE("documentTemplateDirectory", "DocumentTemplateDirectory/[0-9]+\$", RequestMethod.DELETE, "Delete DocumentTemplateDirectory"),

    LOAN_PRODUCT_ADD("LoanProduct", "LoanProduct\$", RequestMethod.POST, "Add loanProduct"),
    LOAN_PRODUCT_GET_LIST("LoanProduct", "LoanProduct(\\?.*|\$)", RequestMethod.GET, "View loanProduct list"),
    LOAN_PRODUCT_GET_ONE("LoanProduct", "LoanProduct/[0-9]+\$", RequestMethod.GET, "View loanProduct one"),
    LOAN_PRODUCT_MODIFY("LoanProduct", "LoanProduct/[0-9]+\$", RequestMethod.PUT, "Modify loanProduct"),
    LOAN_PRODUCT_MODIFY_STATUS("LoanProduct", "LoanProduct/status/[0-9]+\$", RequestMethod.PUT, "Modify loanProduct status"),
    LOAN_PRODUCT_GET_LIST_BY_CODE("LoanProduct","LoanProduct/(.*?)/retrieve",RequestMethod.GET,"View identificationCode loanProduct version list"),
    LOAN_PRODUCT_GET_HISTORY_VERSION_LIST("LoanProduct","LoanProduct/(.*?)/history",RequestMethod.GET,"View identificationCode loanProduct history version list"),
    LOAN_PRODUCT_GET_BASE_INFO_LIST("LoanProduct","LoanProduct/allByStatus",RequestMethod.GET,"View identificationCode loanProduct base info list"),
    LOAN_UPLOAD_CONFIGURE_ADD("LoanUploadConfigure", "LoanUploadConfigure\$", RequestMethod.POST, "Add LoanUploadConfigure"),
    LOAN_UPLOAD_CONFIGURE_GET_LIST("LoanUploadConfigure", "LoanUploadConfigure(\\?.*|\$)", RequestMethod.GET, "View LoanUploadConfigure list"),
    LOAN_UPLOAD_CONFIGURE_DELETE("LoanUploadConfigure", "LoanUploadConfigure/[0-9]+\$", RequestMethod.DELETE, "Modify LoanUploadConfigure status"),

    RISK_CONTROL_RULE_ADD("RiskControlRule", "RiskControlRule\$", RequestMethod.POST, "Add RiskControlRule"),
    RISK_CONTROL_RULE_LIST("RiskControlRule", "RiskControlRule(\\?.*|\$)", RequestMethod.GET, "View RiskControlRule list"),
    RISK_CONTROL_RULE_SORT("RiskControlRule", "RiskControlRule/sort\$", RequestMethod.PUT, "change RiskControlRule sort"),
    RISK_CONTROL_RULE_MODIFY("RiskControlRule", "RiskControlRule/[0-9]+\$", RequestMethod.PUT, "Modify RiskControlRule"),
    RISK_CONTROL_RULE_DELETE("RiskControlRule", "RiskControlRule/[0-9]+\$", RequestMethod.DELETE, "Delete RiskControlRule"),


    MENU_GET_LIST("menu","menus(\\?.*|\$)",RequestMethod.GET,"View menu"),
    USER_MENU("menu","userConfig", RequestMethod.GET,"View user menu"),
    ROLE_MENU("menu","roleConfig", RequestMethod.GET,"View role menu"),
    PERMISSION_MENU("menu","permissionConfig", RequestMethod.GET,"View permission menu"),
    FORMAL_TENANT_MENU("menu","tenantConfig", RequestMethod.GET,"View formal tenant menu"),
    RATE_PLAN_MENU("menu","ratePlanConfig", RequestMethod.GET,"View formal tenant menu"),
    DOCUMENT_TEMPLATE_MENU("menu","documentTemplateConfig", RequestMethod.GET,"View formal documentTemplateConfig menu"),
    LOAN_PRODUCT_MENU("menu","loanProductConfig", RequestMethod.GET,"View formal loanProductConfig menu"),
    LOAN_UPLOAD_CONFIGURE_MENU("menu","loanFileUploadConfig", RequestMethod.GET,"View formal loanUploadConfig menu"),
}