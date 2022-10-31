package cn.sunline.saas.workflow.defintion.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class ProcessDefinitionNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.PROCESS_DEFINITION_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)