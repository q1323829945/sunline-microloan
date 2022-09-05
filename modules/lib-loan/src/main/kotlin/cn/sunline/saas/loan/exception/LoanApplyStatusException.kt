package cn.sunline.saas.loan.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException
import cn.sunline.saas.exceptions.SuccessRequestException

class LoanApplyStatusException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.LOAN_APPLY_STATUS_CAN_NOT_BE_CHANGE,
) : BusinessException(exceptionMessage, statusCode)