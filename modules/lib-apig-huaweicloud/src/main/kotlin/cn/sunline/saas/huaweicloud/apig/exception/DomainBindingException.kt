package cn.sunline.saas.huaweicloud.apig.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.SystemException

class DomainBindingException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DOMAIN_BINDING_ERROR
) : SystemException(exceptionMessage, statusCode)