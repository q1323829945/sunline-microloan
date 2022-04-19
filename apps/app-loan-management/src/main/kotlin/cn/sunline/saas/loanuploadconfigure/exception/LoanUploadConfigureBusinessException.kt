package cn.sunline.saas.loanuploadconfigure.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException


class LoanUploadConfigureBusinessException  (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DATA_ALREADY_EXIST
) : NotFoundException(exceptionMessage, statusCode)