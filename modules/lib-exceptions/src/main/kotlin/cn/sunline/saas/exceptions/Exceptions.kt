package cn.sunline.saas.exceptions

open class BaseException(
        val statusCode: ManagementExceptionCode,
        val exceptionMessage: String? = null,
        val user: Long? = null,
        val data: Any? = null
):Exception()

class ManagementException(
    statusCode: ManagementExceptionCode,
    exceptionMessage: String? = null,
    user: Long? = null,
    data: Any? = null
) : BaseException(statusCode, exceptionMessage, user, data)

class AuthenticationException(
    statusCode: ManagementExceptionCode,
    exceptionMessage: String? = null,
    user: Long? = null
) : BaseException(statusCode, exceptionMessage, user, null)

class NotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.NOT_FOUND_DATA,
    user: Long? = null
) : BaseException(statusCode, exceptionMessage, user, null)

open class BusinessException(
    val exceptionMessage: String? = null,
    val statusCode: ManagementExceptionCode,
    val user: Long? = null
) : Exception()


class UploadException(
       exceptionMessage: String? = null,
       statusCode: ManagementExceptionCode,
):BaseException(statusCode, exceptionMessage, null, null)