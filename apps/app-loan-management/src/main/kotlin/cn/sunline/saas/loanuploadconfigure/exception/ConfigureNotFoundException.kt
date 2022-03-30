package cn.sunline.saas.loanuploadconfigure.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class ConfigureNotFoundException  (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.LOAN_UPLOAD_CONFIGURE_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)