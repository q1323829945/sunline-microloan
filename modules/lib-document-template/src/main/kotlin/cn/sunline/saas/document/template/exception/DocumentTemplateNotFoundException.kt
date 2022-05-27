package cn.sunline.saas.document.template.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class DocumentTemplateNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DOCUMENT_TEMPLATE_NOT_FOUND,
) : NotFoundException(exceptionMessage, statusCode)