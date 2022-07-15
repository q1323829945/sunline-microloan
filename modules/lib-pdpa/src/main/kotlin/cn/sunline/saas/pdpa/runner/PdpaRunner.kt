package cn.sunline.saas.pdpa.runner

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.pdpa.services.PdpaAuthorityService
import org.springframework.boot.CommandLineRunner
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class PdpaRunner(
    private val pdpaAuthorityService: PdpaAuthorityService,
    private val tenantService: TenantService): CommandLineRunner {

    override fun run(vararg args: String?) {
        val tenants = getTenants()
        tenants.forEach {
            ContextUtil.setTenant(it.id.toString())
            pdpaAuthorityService.register()
        }
    }


    private fun getTenants():List<Tenant>{
        val tenants = tenantService.getPaged(null, Pageable.unpaged())
        return tenants.content
    }

}