package cn.sunline.saas.loan.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.SuccessRequestException

class SubTypeIsIrrelevantParentTypeException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.SUB_TYPE_IS_IRRELEVANT_PARENT_TYPE,
) : SuccessRequestException(exceptionMessage, statusCode)