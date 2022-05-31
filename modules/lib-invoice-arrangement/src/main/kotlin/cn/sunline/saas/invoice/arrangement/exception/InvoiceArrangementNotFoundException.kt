package cn.sunline.saas.invoice.arrangement.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: InvoiceArrangementNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/26 16:09
 */
class InvoiceArrangementNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.INVOICE_ARRANGEMENT_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)