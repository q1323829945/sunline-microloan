package cn.sunline.saas.rpc.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class RatePlanNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.RATE_PLAN_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)