package cn.sunline.saas.consumer_loan.exception

import cn.sunline.saas.exceptions.BadRequestException
import cn.sunline.saas.exceptions.ManagementExceptionCode

/**
 * @title: LoanAgreementStattusCheckException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/7 10:45
 */
class LoanAgreementStatusCheckException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.LOAN_AGREEMENT_STATUS_CHECK
) : BadRequestException(
    exceptionMessage,
    statusCode
)