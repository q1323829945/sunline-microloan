package cn.sunline.saas.tenant.service

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.tenant.service.dto.DTOTenant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import java.util.*

@Service
class TenantManagerService(
    private val sequence: Sequence
) {
    @Autowired
    private lateinit var tenantService: TenantService


    fun saveTenant(dtoTenant: DTOTenant){
        val tenant = tenantService.getOne(dtoTenant.id)
        if(tenant != null){
            updateTenant(tenant,dtoTenant)
        } else {
            addTenant(dtoTenant)
        }
    }

    fun addTenant(dtoTenant: DTOTenant){

        val tenant = Tenant(
            name = dtoTenant.name,
            country = CountryType.values().first { it.countryName.contains(dtoTenant.country) },
            enabled = true,
            saasUUID = UUID.fromString(dtoTenant.saasUUID),
        )

        tenantService.save(tenant)


    }

    fun updateTenant(tenant:Tenant,dtoTenant: DTOTenant){

        tenant.enabled = dtoTenant.enabled

        tenantService.save(tenant)

    }
}

