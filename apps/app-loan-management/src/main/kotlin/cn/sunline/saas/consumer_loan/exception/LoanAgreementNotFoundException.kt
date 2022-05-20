package cn.sunline.saas.consumer_loan.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: UserNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/29 9:59
 */
class LoanAgreementNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.LOAN_AGREEMENT_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)