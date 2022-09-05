package cn.sunline.saas.channel.rbac.exception

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode

class PersonAlreadyBindingException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.PERSON_ALREADY_BINDING
): ManagementException(statusCode,exceptionMessage)