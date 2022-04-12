package cn.sunline.saas.global.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.SystemException

/**
 * @title: TenanContextNullException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/11 16:45
 */
class ContextNullException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.CONTEXT_IS_NULL
) : SystemException(exceptionMessage, statusCode)