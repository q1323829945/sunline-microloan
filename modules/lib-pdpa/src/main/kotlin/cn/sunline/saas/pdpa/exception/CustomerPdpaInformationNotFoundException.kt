package cn.sunline.saas.pdpa.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class CustomerPdpaInformationNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.CUSTOMER_PDPA_INFORMATION_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)