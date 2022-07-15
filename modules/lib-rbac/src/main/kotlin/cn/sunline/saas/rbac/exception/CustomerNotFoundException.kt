package cn.sunline.saas.rbac.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class CustomerNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.CUSTOMER_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)