package cn.sunline.saas.workflow.step.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class EventStepNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.EVENT_STEP_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)