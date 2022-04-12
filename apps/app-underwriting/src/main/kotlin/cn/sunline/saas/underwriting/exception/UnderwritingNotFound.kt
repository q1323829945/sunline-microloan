package cn.sunline.saas.underwriting.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: UnderwritingNotFound
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/12 15:37
 */
class UnderwritingNotFound(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.UNDERWRITING_NOT_FOUND,
) : NotFoundException(
    exceptionMessage, statusCode
)