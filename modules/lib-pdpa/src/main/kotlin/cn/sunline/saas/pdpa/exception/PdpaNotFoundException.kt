package cn.sunline.saas.pdpa.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class PdpaNotFoundException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.PDPA_NOT_FOUND
): NotFoundException(exceptionMessage,statusCode)