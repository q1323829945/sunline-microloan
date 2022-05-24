package cn.sunline.saas.global.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: TenantNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/24 15:00
 */
class UserUninitializedException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.USER_UNINITIALIZED
) : NotFoundException(exceptionMessage, statusCode) {
}