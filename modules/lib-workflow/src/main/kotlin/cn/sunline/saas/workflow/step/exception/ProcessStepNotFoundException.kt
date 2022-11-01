package cn.sunline.saas.workflow.step.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class ProcessStepNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.PROCESS_STEP_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)