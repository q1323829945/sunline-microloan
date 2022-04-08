package cn.sunline.saas.huaweicloud.apig.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.SystemException

class DNSException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DNS_ERROR
) : SystemException(exceptionMessage, statusCode)