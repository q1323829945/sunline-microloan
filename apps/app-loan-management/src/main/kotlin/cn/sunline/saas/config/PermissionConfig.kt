package cn.sunline.saas.config

import org.springframework.web.bind.annotation.GetMapping
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
    LOAN_UPLOAD_ALL_LIST("LoanUploadConfigure", "LoanUploadConfigure/all", RequestMethod.GET, "View loanUploadConfigure list"),
    LOAN_UPLOAD_CONFIGURE_GET_LIST("LoanUploadConfigure", "LoanUploadConfigure(\\?.*|\$)", RequestMethod.GET, "View LoanUploadConfigure list"),
    LOAN_UPLOAD_CONFIGURE_DELETE("LoanUploadConfigure", "LoanUploadConfigure/[0-9]+\$", RequestMethod.DELETE, "Modify LoanUploadConfigure status"),

    RISK_CONTROL_RULE_ADD("RiskControlRule", "RiskControlRule\$", RequestMethod.POST, "Add RiskControlRule"),
    RISK_CONTROL_RULE_LIST("RiskControlRule", "RiskControlRule(\\?.*|\$)", RequestMethod.GET, "View RiskControlRule list"),
    RISK_CONTROL_RULE_SORT("RiskControlRule", "RiskControlRule/sort\$", RequestMethod.PUT, "change RiskControlRule sort"),
    RISK_CONTROL_RULE_MODIFY("RiskControlRule", "RiskControlRule/[0-9]+\$", RequestMethod.PUT, "Modify RiskControlRule"),
    RISK_CONTROL_RULE_DELETE("RiskControlRule", "RiskControlRule/[0-9]+\$", RequestMethod.DELETE, "Delete RiskControlRule"),
    RISK_CONTROL_RULE_GET_ONE("RiskControlRule", "RiskControlRule/[0-9]+\$", RequestMethod.GET, "Get One RiskControlRule"),

    ORGANISATION_ADD("Organisation", "Organisation\$", RequestMethod.POST, "Add Organisation"),
    ORGANISATION_LIST("Organisation", "Organisation(\\?.*|\$)", RequestMethod.GET, "View Organisation list"),
    ORGANISATION_MODIFY("Organisation", "Organisation/[0-9]+\$", RequestMethod.PUT, "Modify Organisation"),
    ORGANISATION_GET_ONE("Organisation", "Organisation/[0-9]+\$", RequestMethod.GET, "Get One Organisation"),

    BUSINESS_UNIT_GET_ALL("BusinessUnit", "BusinessUnit\$", RequestMethod.GET, "Get All BusinessUnit"),


    PERSON_ADD("Person", "Person\$", RequestMethod.POST, "Add Person"),
    PERSON_LIST("Person", "Person(\\?.*|\$)", RequestMethod.GET, "View Person list"),
    PERSON_MODIFY("Person", "Person/[0-9]+\$", RequestMethod.PUT, "Modify Person"),
    PERSON_GET_ONE("Person", "Person/[0-9]+\$", RequestMethod.GET, "Get One Person"),

    CUSTOMER_OFFER_LIST("CustomerOffer", "CustomerOffer(\\?.*|\$)", RequestMethod.GET, "View CustomerOffer list"),
    CUSTOMER_OFFER_MODIFY("CustomerOffer", "CustomerOffer/(PASS|REJECT)/[0-9]+\$", RequestMethod.PUT, "Modify CustomerOffer"),
    CUSTOMER_OFFER_GET_ONE("CustomerOffer", "CustomerOffer/[0-9]+\$", RequestMethod.GET, "Get One CustomerOffer"),
    CUSTOMER_OFFER_DOWNLOAD("CustomerOffer", "CustomerOffer/download(\\?.*|\$)", RequestMethod.GET, "download"),

    UNDERWRITING_MANAGEMENT_LIST("UnderwritingManagement", "UnderwritingManagement(\\?.*|\$)", RequestMethod.GET, "View UnderwritingManagement list"),
    UNDERWRITING_MANAGEMENT_GET_ONE("UnderwritingManagement", "UnderwritingManagement/[0-9]+\$", RequestMethod.GET, "Get One UnderwritingManagement"),
    UNDERWRITING_MANAGEMENT_APPROVAL("UnderwritingManagement", "UnderwritingManagement/approval/[0-9]+\$", RequestMethod.PUT, "Update UnderwritingManagement status"),
    UNDERWRITING_MANAGEMENT_REJECTED("UnderwritingManagement", "UnderwritingManagement/rejected/[0-9]+\$", RequestMethod.PUT, "Update UnderwritingManagement status"),

    LOAN_AGREEMENT_MANAGEMENT_LIST("LoanAgreement", "LoanAgreement(\\?.*|\$)", RequestMethod.GET, "View LoanAgreement list"),
    LOAN_AGREEMENT_MANAGEMENT_PAID("LoanAgreement", "LoanAgreement/paid/[0-9]+\$", RequestMethod.PUT, "LoanAgreement paid"),
    LOAN_AGREEMENT_MANAGEMENT_SIGNED("LoanAgreement", "LoanAgreement/signed/[0-9]+\$", RequestMethod.PUT, "LoanAgreement signed"),

    PDPA_INFORMATION("pdpa","pdpa/(.*?)/retrieve", RequestMethod.GET,"View pdpa information"),
    PDPA_ADD("pdpa","pdpa\$", RequestMethod.POST,"add pdpa information"),
    PDPA_LIST("pdpa","pdpa(\\?.*|\$)", RequestMethod.GET,"get pdpa information list"),
    PDPA_MODIFY("pdpa","pdpa/[0-9]+\$", RequestMethod.PUT,"put pdpa information"),
    PDPA_GET_ONE("pdpa","pdpa/[0-9]+\$", RequestMethod.GET,"get pdpa information"),
    PDPA_AUTHORIZATION_GET_ONE("pdpa","pdpa/authorization\$", RequestMethod.GET,"get pdpa authorization information"),
    PDPA_AUTHORIZATION_UPDATE("pdpa","pdpa/authorization\$", RequestMethod.PUT,"update pdpa authorization information"),

    MENU_GET_LIST("menu","menus(\\?.*|\$)",RequestMethod.GET,"View menu"),
    USER_MENU("menu","userConfig", RequestMethod.GET,"View user menu"),
    ROLE_MENU("menu","roleConfig", RequestMethod.GET,"View role menu"),
    PERMISSION_MENU("menu","permissionConfig", RequestMethod.GET,"View permission menu"),
    FORMAL_TENANT_MENU("menu","tenantConfig", RequestMethod.GET,"View formal tenant menu"),
    RATE_PLAN_MENU("menu","ratePlanConfig", RequestMethod.GET,"View formal tenant menu"),
    DOCUMENT_TEMPLATE_MENU("menu","documentTemplateConfig", RequestMethod.GET,"View formal documentTemplateConfig menu"),
    LOAN_PRODUCT_MENU("menu","loanProductConfig", RequestMethod.GET,"View formal loanProductConfig menu"),
    RISK_CONTROL_RULE_MENU("menu","riskControlRuleConfig", RequestMethod.GET,"View formal RiskControlRuleConfig menu"),
    ORGANISATION_MENU("menu","organisationConfig", RequestMethod.GET,"View formal OrganisationConfig menu"),
    EMPLOYEE_MENU("menu","employeeConfig", RequestMethod.GET,"View formal employeeConfig menu"),
    CUSTOMER_MENU("menu","customerConfig", RequestMethod.GET,"View formal customerConfig menu"),
    CUSTOMER_OFFER_MENU("menu","customerOfferConfig", RequestMethod.GET,"View formal customerOfferConfig menu"),
    LOAN_AGREEMENT_MANAGEMENT_MENU("menu","loanAgreementManagementConfig", RequestMethod.GET,"View formal loanAgreementManagementConfig menu"),
    REPAYMENT_MANAGEMENT_MENU("menu","repaymentManagementConfig", RequestMethod.GET,"View formal repaymentManagementConfig menu"),
    PDPA_MENU("menu","pdpaConfig", RequestMethod.GET,"View pdpaConfig menu"),
    PDPA_AUTHORIZATION_MENU("menu","pdpaAuthorizationConfig", RequestMethod.GET,"View pdpaAuthorizationConfig menu"),

    CUSTOMER_LOAN_INVOICE_CURRENT("ConsumerLoan","ConsumerLoan/repayment/instruction(\\?.*|\$)",RequestMethod.GET,"View invoice information"),
    CUSTOMER_LOAN_INVOICE_HISTORY("ConsumerLoan","ConsumerLoan/invoice/(.*?)/history",RequestMethod.GET,"View invoice history information"),
    CUSTOMER_LOAN_INVOICE_FINISH("ConsumerLoan","ConsumerLoan/invoice/finish/[0-9]+\$",RequestMethod.PUT,"finish invoice"),
    CUSTOMER_LOAN_INVOICE_CANCEL("ConsumerLoan","ConsumerLoan/invoice/cancel/[0-9]+\$",RequestMethod.PUT,"cancel invoice"),
    CUSTOMER_LOAN_INVOICE_MODIFY("ConsumerLoan", "ConsumerLoan/repayment/instruction/(FAILED|FULFILLED)/[0-9]+\$", RequestMethod.PUT, "Modify invoice"),
    CUSTOMER_LOAN_INVOICE_DETAIL("ConsumerLoan","ConsumerLoan/invoice/detail/(.*?)/retrieve",RequestMethod.GET,"View invoice details"),


    LOAN_BUSINESS_GET_LIST("LoanBusiness","LoanBusiness/(\\?.*|\$)",RequestMethod.GET,"View loan information"),
    LOAN_BUSINESS_APPLICATION_RETRIEVE("LoanBusiness","LoanBusiness/application/{applicationId}/retrieve",RequestMethod.GET,"View loan application information"),
    LOAN_BUSINESS_FEE_RETRIEVE("LoanBusiness","LoanBusiness/fee/{applicationId}/retrieve",RequestMethod.GET,"view loan fee information"),
    LOAN_BUSINESS_HISTORY_EVENT_RETRIEVE("LoanBusiness","LoanBusiness/history/event/{applicationId}/retrieve",RequestMethod.GET,"view loan history event record"),
    LOAN_BUSINESS_REPAYMENT_RECORD_RETRIEVE("LoanBusiness", "LoanBusiness/repayment/record/{agreementId}/retrieve", RequestMethod.GET, "view repayment record"),
    LOAN_BUSINESS_DISBURSEMENT_RETRIEVE("LoanBusiness", "LoanBusiness/disbursement/{agreementId}/retrieve", RequestMethod.GET, "view disbursement disbursement")

}

