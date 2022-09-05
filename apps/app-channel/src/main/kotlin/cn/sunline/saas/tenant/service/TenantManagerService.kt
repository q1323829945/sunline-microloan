package cn.sunline.saas.tenant.service

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.model.TenantPermission
import cn.sunline.saas.multi_tenant.services.TenantPermissionService
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantUtil
import cn.sunline.saas.tenant.service.dto.DTOTenant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence

@Service
class TenantManagerService(
    private val sequence: Sequence
) {
    @Autowired
    private lateinit var tenantService: TenantService

    @Autowired
    private lateinit var tenantPermissionService: TenantPermissionService

    fun saveTenant(dtoTenant: DTOTenant){
        val tenant = tenantService.getOne(dtoTenant.id)
        if(tenant != null){
            updateTenant(tenant,dtoTenant)
        } else {
            addTenant(dtoTenant)
        }
    }

    fun addTenant(dtoTenant: DTOTenant){
        val tenantPermissions = mutableListOf<TenantPermission>()

        dtoTenant.permissions?.map {
            val tenantPermission = TenantPermission(
                sequence.nextId(),
                productApplicationId = it.productApplicationId,
                tenantId = dtoTenant.id,
                enabled = true,
                subscriptionId = it.subscriptionId
            )
            tenantPermissions += tenantPermissionService.save(tenantPermission)
        }

        val tenant = Tenant(
            id = dtoTenant.id,
            country = CountryType.values().first { it.countryName.contains(dtoTenant.country) },
            enabled = true,
            permissions = tenantPermissions
        )

        TenantUtil.setTenant(tenant.id,tenant)

        tenantService.save(tenant)
    }

    fun updateTenant(tenant:Tenant,dtoTenant: DTOTenant){
        val tenantPermissions = mutableSetOf<TenantPermission>()

        tenant.permissions?.map { it.productApplicationId }?.run {
            dtoTenant.permissions?.forEach {
                if(!this.contains(it.productApplicationId)){
                    tenantPermissions += tenantPermissionService.save(TenantPermission(
                        sequence.nextId(),
                        productApplicationId = it.productApplicationId,
                        tenantId = dtoTenant.id,
                        enabled = true,
                        subscriptionId = it.subscriptionId
                    ))
                }
            }
        }

        dtoTenant.permissions?.map { it.productApplicationId }?.run {
            tenant.permissions?.filter { !it.enabled }?.forEach {
                if(this.contains(it.productApplicationId)){
                    it.enabled = true
                }
            }

            tenant.permissions?.forEach {
                if(!this.contains(it.productApplicationId)){
                    it.enabled = false
                }
                tenantPermissions += it
            }
        }

        tenant.enabled = dtoTenant.enabled
        tenant.permissions = tenantPermissions.toMutableList()

        tenantService.save(tenant)

    }
}

