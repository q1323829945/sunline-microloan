package cn.sunline.saas.interest.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode


class InterestRateBusinessException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DATA_ALREADY_EXIST
) : BusinessException(exceptionMessage, statusCode)