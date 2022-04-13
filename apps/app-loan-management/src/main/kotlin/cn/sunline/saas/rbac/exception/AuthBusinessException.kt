package cn.sunline.saas.rbac.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode


class AuthBusinessException  (
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.AUTHORIZATION_LOGIN_FAILED
) : BusinessException(exceptionMessage, statusCode)