package cn.sunline.saas.product.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode


class LoanProductNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.PRODUCT_NOT_FOUND
) : BusinessException(exceptionMessage, statusCode)