package cn.sunline.saas.repayment.instruction.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException


class RepaymentInstructionNotFoundException (
    exceptionMessage: String,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.REPAYMENT_INSTRUCTION_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)