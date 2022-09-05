package cn.sunline.saas.loan.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode

class LoanAgentHasBeenProductTypeException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.LOAN_HAS_BEEN_PRODUCT_TYPE,
) : BusinessException(exceptionMessage, statusCode)