package cn.sunline.saas.interest.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode

/**
 * @title: InterestRateNullException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/23 14:46
 */
class InterestRateNullException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.BASE_RATE_NULL
) : BusinessException(
    exceptionMessage,
    statusCode
)