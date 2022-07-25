package cn.sunline.saas.dapr_wrapper.runner

import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class TenantRunner: CommandLineRunner {
    @Autowired
    private lateinit var tenantService: TenantService

    override fun run(vararg args: String?) {
        val tenants = getTenants()
        tenants.forEach {
            TenantUtil.setTenant(it.id,it)
        }
    }


    private fun getTenants():List<Tenant>{
        val tenants = tenantService.getPaged(null, Pageable.unpaged())
        return tenants.content
    }
}