package cn.sunline.saas.pdpa.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class PdpaAlreadyExistException (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.PDPA_ALREADY_EXIST
): NotFoundException(exceptionMessage,statusCode)