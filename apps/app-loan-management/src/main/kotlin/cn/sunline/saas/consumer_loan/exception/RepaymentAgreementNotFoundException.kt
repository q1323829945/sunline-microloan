package cn.sunline.saas.consumer_loan.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class RepaymentAgreementNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.REPAYMENT_ARRANGEMENT_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)