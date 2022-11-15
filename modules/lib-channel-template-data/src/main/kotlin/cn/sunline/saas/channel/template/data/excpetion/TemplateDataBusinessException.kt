package cn.sunline.saas.channel.template.data.excpetion

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException


class TemplateDataBusinessException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.CONTEXT_IS_NULL
): BusinessException(exceptionMessage,statusCode)