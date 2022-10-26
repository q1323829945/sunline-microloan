package cn.sunline.saas.workflow.defintion.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class ActivityDefinitionNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.ACTIVITY_DEFINITION_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)