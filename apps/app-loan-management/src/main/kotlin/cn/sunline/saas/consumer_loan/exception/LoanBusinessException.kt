package cn.sunline.saas.consumer_loan.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException


class LoanBusinessException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DATA_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)