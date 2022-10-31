package cn.sunline.saas.workflow.defintion.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode

class ProcessDefinitionAlreadyExistException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.PROCESS_DEFINITION_ALREADY_EXIST
): BusinessException(exceptionMessage,statusCode)