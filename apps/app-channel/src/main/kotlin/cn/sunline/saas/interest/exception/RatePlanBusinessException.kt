package cn.sunline.saas.interest.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode



class RatePlanBusinessException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.RATE_PLAN_DATA_ALREADY_EXIST
) : BusinessException(exceptionMessage, statusCode)