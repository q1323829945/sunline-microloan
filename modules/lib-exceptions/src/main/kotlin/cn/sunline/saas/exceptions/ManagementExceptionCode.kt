package cn.sunline.saas.exceptions

enum class ManagementExceptionCode(val code: Int) {
    // Base 1000 - 1999
    REQUEST_INVALID_PARAMETER(1000),
    REQUEST_INCOMPLETE_PARAMETER(1001),
    REQUEST_MISSING_REQUEST_PARAMETER(1002),
    REQUEST_RESOURCE_ACCESS_DENIED(1004),
    DATA_ALREADY_EXIST(1005),
    TYPE_ERROR(1006),
    DATA_NOT_FOUND(1007),
    CONTEXT_IS_NULL(1008),

    // Auth 2000 - 2999
    AUTHORIZATION_LOGIN_FAILED(2000),
    AUTHORIZATION_TOKEN_VALIDATION_FAILED(2001),
    AUTHORIZATION_USERNAME_MISSING(2002),
    AUTHORIZATION_USERNAME_INVALID(2003),
    AUTHORIZATION_TENANT_DOMAIN_MISSING(2004),
    USER_NOT_FOUND(2005),
    ROLE_NOT_FOUND(2006),

    // Tenant 3000 - 3999
    TENANT_DATA_SOURCE_INVALID(3000),

    //upload 4000 - 4999
    // Obs and Document 4000 - 4999
    FILE_UPLOAD_FAILED(4000),
    BODY_TYPE_ERROR(4001),
    DOCUMENT_TEMPLATE_NOT_FOUND(4002),
    DOCUMENT_DIRECTORY_NOT_FOUND(4003),

    // Product 5000 - 5049
    PRODUCT_NOT_FOUND(5000),

    // Fee 5050 - 5099
    FEE_CONFIG_ERROR(5050),

    // Interest 5100 - 5149
    RATE_PLAN_NOT_FOUND(5100),
    INTEREST_RATE_NOT_FOUND(5101),

    // Http 5150 - 5199
    HTTP_ERROR(5151),

    //loan 5200 - 5299
    LOAN_UPLOAD_CONFIGURE_NOT_FOUND(5101),

    //huawei cloud 5300 - 5349
    DOMAIN_BINDING_ERROR(5300),
    CERTIFICATE_BINDING_ERROR(5301),


    //underwriting 5350 - 5399
    UNDERWRITING_NOT_FOUND(5350),

    //risk control rule 5400-5450
    RISK_CONTROL_RULE_NOT_FOUND(5400),


}