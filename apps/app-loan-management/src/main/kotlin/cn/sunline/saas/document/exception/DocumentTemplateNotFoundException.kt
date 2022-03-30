package cn.sunline.saas.document.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: DocumentTemplateNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/29 9:15
 */
class DocumentTemplateNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DOCUMENT_TEMPLATE_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)