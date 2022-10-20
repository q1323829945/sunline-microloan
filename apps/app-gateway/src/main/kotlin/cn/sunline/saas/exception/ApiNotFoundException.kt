package cn.sunline.saas.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class ApiNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.API_NOT_FOUND,
    user: Long? = null,
) : NotFoundException(exceptionMessage, statusCode, user)