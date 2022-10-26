package cn.sunline.saas.workflow.step.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class ActivityStepNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.ACTIVITY_STEP_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)