package cn.sunline.saas.rbac.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: UserNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/29 9:59
 */
class UserNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.USER_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode)