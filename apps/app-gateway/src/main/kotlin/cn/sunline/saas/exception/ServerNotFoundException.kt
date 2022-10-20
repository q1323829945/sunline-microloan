package cn.sunline.saas.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class ServerNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.SERVER_NOT_FOUND,
    user: Long? = null,
) : NotFoundException(exceptionMessage, statusCode, user)