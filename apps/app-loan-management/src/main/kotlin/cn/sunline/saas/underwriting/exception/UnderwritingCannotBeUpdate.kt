package cn.sunline.saas.underwriting.exception

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class UnderwritingCannotBeUpdate(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.UNDERWRITING_CANNOT_BE_UPDATE,
) : ManagementException(
    statusCode,exceptionMessage
)