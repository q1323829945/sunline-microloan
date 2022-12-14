package cn.sunline.saas.loan.agreement.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: LoanAgreementNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/7 10:05
 */
class LoanAgreementNotFoundException(
    exceptionMessage: String?=null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.LOAN_AGREEMENT_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)