package cn.sunline.saas.rpc.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class InterestRateNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.INTEREST_RATE_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)