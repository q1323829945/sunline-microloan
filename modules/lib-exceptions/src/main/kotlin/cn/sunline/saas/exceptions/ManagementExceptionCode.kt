package cn.sunline.saas.exceptions

enum class ManagementExceptionCode(val code: Int) {
    // Base 1000 - 1999
    REQUEST_INVALID_PARAMETER(1000),
    REQUEST_INCOMPLETE_PARAMETER(1001),
    REQUEST_MISSING_REQUEST_PARAMETER(1002),
    NOT_FOUND_DATA(1003),

    // Auth 2000 - 2999
    AUTHORIZATION_LOGIN_FAILED(2000),
    AUTHORIZATION_TOKEN_VALIDATION_FAILED(2001),
    AUTHORIZATION_USERNAME_MISSING(2002),
    AUTHORIZATION_USERNAME_INVALID(2003),
    AUTHORIZATION_TENANT_DOMAIN_MISSING(2004),
    AUTHORIZATION_TENANT_FAILED(2005),

    // Tenant 3000 - 3999
    TENANT_DATA_SOURCE_INVALID(3000),

}