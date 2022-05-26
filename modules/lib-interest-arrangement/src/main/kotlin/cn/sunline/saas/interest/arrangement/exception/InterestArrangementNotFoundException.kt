package cn.sunline.saas.interest.arrangement.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: InterestArrangementNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 16:35
 */
class InterestArrangementNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.INTEREST_ARRANGEMENT_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)