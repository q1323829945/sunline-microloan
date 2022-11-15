package cn.sunline.saas.pdpa.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class PDPANotFoundException (
    exceptionMessage: String,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.PDPA_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)