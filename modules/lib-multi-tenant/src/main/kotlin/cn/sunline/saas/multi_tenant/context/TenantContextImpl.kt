package cn.sunline.saas.multi_tenant.context

import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.repository.TenantRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

/**
 * @title: TenantContextImpl
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/28 14:03
 */
@Component
class TenantContextImpl(private val tenantRepo: TenantRepository) : TenantContext {

    private val context = ThreadLocal<Tenant>()


    override fun get(): Tenant? {
        return context.get()
    }

    override fun set(tenantId: String) {
        var tenant = tenantRepo.findByIdOrNull(tenantId.toLong())
        if(tenant == null){
            //TODO remote call tenant mananger service
        }
        context.set(tenant)
    }

    fun remove() {
        context.remove()
    }
}