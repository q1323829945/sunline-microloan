package cn.sunline.saas.workflow.defintion.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode

class ProcessDefinitionCodeException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.PROCESS_DEFINITION_CODE_ERROR
): BusinessException(exceptionMessage,statusCode)