package cn.sunline.saas.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class InstanceNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.INSTANCE_NOT_FOUND,
    user: Long? = null,
) : NotFoundException(exceptionMessage, statusCode, user)