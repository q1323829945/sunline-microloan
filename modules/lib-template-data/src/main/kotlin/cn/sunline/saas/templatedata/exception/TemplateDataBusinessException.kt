package cn.sunline.saas.templatedata.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException


class TemplateDataBusinessException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DATA_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)