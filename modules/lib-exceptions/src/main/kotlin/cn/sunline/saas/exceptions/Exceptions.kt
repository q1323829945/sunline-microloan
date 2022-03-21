package cn.sunline.saas.exceptions

open class BaseException(
        val statusCode: ManagementExceptionCode,
        val exceptionMessage: String? = null,
        val user: Long? = null,
        val data: Any? = null
):Exception()

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

open class BusinessException(
    val exceptionMessage: String? = null,
    val statusCode: ManagementExceptionCode,
    val user: Long? = null
) : Exception()


class UploadException(
       exceptionMessage: String? = null,
       statusCode: ManagementExceptionCode,
):BaseException(statusCode, exceptionMessage, null, null)