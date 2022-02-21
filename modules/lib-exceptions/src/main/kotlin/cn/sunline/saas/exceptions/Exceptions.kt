package cn.sunline.saas.exceptions

data class ManagementException(
    val statusCode: ManagementExceptionCode,
    val exceptionMessage: String? = null,
    val user: Long? = null,
    val data: Any? = null
) : Exception()

data class AuthenticationException(
    val statusCode: ManagementExceptionCode,
    val exceptionMessage: String? = null,
    val user: Long? = null
) : Exception()

data class NotFoundException(
    val exceptionMessage: String? = null,
    val statusCode: ManagementExceptionCode = ManagementExceptionCode.NOT_FOUND_DATA,
    val user: Long? = null
) : Exception()