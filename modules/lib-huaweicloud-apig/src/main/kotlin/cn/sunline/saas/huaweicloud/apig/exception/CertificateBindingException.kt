package cn.sunline.saas.huaweicloud.apig.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.SystemException

class CertificateBindingException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.CERTIFICATE_BINDING_ERROR
) : SystemException(exceptionMessage, statusCode)