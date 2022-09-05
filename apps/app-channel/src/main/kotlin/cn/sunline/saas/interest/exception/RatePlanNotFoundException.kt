package cn.sunline.saas.interest.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: RatePlanNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/29 9:09
 */
class RatePlanNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.RATE_PLAN_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)