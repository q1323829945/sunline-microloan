package cn.sunline.saas.multi_tenant.context

import cn.sunline.saas.multi_tenant.model.Tenant

/**
 * @title: TenantUtil
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/25 14:11
 */
interface TenantContext {

    fun get(): Tenant?

    fun set(tenantId: String)
}