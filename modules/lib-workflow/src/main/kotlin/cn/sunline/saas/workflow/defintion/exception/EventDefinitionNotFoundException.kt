package cn.sunline.saas.workflow.defintion.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class EventDefinitionNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.EVENT_DEFINITION_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)