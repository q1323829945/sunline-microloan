package cn.sunline.saas.invoice.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class RepaymentInstructionAlreadyExistsException(
    exceptionMessage: String,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.REPAYMENT_INSTRUCTION_ERROR
) : BusinessException(exceptionMessage, statusCode)