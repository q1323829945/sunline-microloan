package cn.sunline.saas.exceptions

open class BaseException(
    val statusCode: ManagementExceptionCode,
    val exceptionMessage: String? = null,
    val user: Long? = null,
    val data: Any? = null
) : Exception(exceptionMessage)

open class ManagementException(
    statusCode: ManagementExceptionCode,
    exceptionMessage: String? = null,
    user: Long? = null,
    data: Any? = null
) : BaseException(statusCode, exceptionMessage, user, data)

open class AuthenticationException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode,
    user: Long? = null
) : BaseException(statusCode, exceptionMessage, user, null)

open class NotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode ,
    user: Long? = null
) : BaseException(statusCode, exceptionMessage, user, null)

open class BusinessException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode,
    user: Long? = null,
    data:Any? = null,
) : BaseException(statusCode, exceptionMessage, user, data)

open class SystemException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode,
) : BaseException(statusCode, exceptionMessage, null, null)

open class BadRequestException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode,
) : BaseException(statusCode, exceptionMessage, null, null)

open class SuccessRequestException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode,
    user: Long? = null,
    data:Any? = null,
) : BaseException(statusCode, exceptionMessage, user, data)