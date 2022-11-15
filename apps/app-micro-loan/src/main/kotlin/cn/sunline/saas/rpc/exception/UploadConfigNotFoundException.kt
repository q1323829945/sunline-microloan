package cn.sunline.saas.rpc.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

class UploadConfigNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.LOAN_UPLOAD_CONFIGURE_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)