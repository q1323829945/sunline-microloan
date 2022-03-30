package cn.sunline.saas.multi_tenant.context

import org.springframework.stereotype.Component

/**
 * @title: TenantContextImpl
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 14:03
 */
@Component
class TenantContextImpl : TenantContext {

    private val context = ThreadLocal<Long>()

    override fun get(): Long {
        return context.get()
    }

    override fun set(tenantId: String) {
        context.set(tenantId.toLong())
    }

    fun remove() {
        context.remove()
    }
}