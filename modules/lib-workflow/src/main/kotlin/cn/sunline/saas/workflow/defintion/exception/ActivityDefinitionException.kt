package cn.sunline.saas.workflow.defintion.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode

class ActivityDefinitionException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode,
): BusinessException(exceptionMessage,statusCode)