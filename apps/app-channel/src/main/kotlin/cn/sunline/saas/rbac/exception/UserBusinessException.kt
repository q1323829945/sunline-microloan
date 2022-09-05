package cn.sunline.saas.rbac.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode


class UserBusinessException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.DATA_ALREADY_EXIST
) : BusinessException(exceptionMessage, statusCode)