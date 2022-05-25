package cn.sunline.saas.multi_tenant.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.NotFoundException

/**
 * @title: TenantNotFoundException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/24 15:00
 */
class TenantNotFoundException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.TENANT_NOT_FOUND
) : NotFoundException(exceptionMessage, statusCode) {
}