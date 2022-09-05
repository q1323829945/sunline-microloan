package cn.sunline.saas.loan.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException
import cn.sunline.saas.exceptions.SuccessRequestException

class ApplicationIdAlreadyExistException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.APPLICATION_ID_ALREADY_EXIST,
) : BusinessException(exceptionMessage, statusCode)