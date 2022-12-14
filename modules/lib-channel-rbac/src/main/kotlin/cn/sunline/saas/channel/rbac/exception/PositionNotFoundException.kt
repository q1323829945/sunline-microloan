package cn.sunline.saas.channel.rbac.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class PositionNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.POSITION_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)