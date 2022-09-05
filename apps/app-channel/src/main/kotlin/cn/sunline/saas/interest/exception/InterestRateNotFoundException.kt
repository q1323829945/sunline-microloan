package cn.sunline.saas.interest.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: InterestRateNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/29 9:24
 */
class InterestRateNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.INTEREST_RATE_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)