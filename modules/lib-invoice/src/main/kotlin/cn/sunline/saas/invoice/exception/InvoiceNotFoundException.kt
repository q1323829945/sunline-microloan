package cn.sunline.saas.invoice.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: InvoiceNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/6/2 16:10
 */
class InvoiceNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.INVOICE_NOT_FOUND,
) : NotFoundException(
    exceptionMessage,
    statusCode
) {
}