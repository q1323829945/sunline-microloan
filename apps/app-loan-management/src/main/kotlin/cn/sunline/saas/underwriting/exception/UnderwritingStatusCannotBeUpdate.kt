package cn.sunline.saas.underwriting.exception

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: UnderwritingNotFound
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/12 15:37
 */
class UnderwritingStatusCannotBeUpdate(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.UNDERWRITING_STATUS_CANNOT_BE_UPDATE,
) : ManagementException(
    statusCode,exceptionMessage
)