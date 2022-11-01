package cn.sunline.saas.workflow.defintion.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode

class EventTypeHasBeenUsedException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.EVENT_TYPE_HAS_BEEN_USED
): BusinessException(exceptionMessage,statusCode)