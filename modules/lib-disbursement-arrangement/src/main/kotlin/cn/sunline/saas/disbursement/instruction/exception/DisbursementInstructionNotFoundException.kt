package cn.sunline.saas.disbursement.instruction.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: DisbursementInstructionNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/10 16:39
 */
class DisbursementInstructionNotFoundException (
    exceptionMessage: String,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DISBURSEMENT_INSTRUCTION_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)