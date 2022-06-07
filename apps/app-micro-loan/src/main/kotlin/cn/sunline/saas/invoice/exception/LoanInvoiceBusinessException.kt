package cn.sunline.saas.invoice.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException


class LoanInvoiceBusinessException(
    exceptionMessage: String,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DATA_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)