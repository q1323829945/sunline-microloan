package cn.sunline.saas.tenant.service

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.model.TenantPermission
import cn.sunline.saas.multi_tenant.services.TenantPermissionService
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.tenant.service.dto.DTOTenant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TenantManagerService {
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

        tenantService.save(tenant)
    }

    fun updateTenant(tenant:Tenant,dtoTenant: DTOTenant){
        if(tenant.enabled || dtoTenant.enabled == true){
            val tenantPermissions = mutableSetOf<TenantPermission>()

            val productApplicationIds = dtoTenant.permissions?.map {
                val tenantPermission = TenantPermission(
                    productApplicationId = it.productApplicationId,
                    tenantId = dtoTenant.id,
                    enabled = true,
                    subscriptionId = it.subscriptionId
                )
                tenantPermissions += tenantPermissionService.save(tenantPermission)
                it.productApplicationId
            }

            if(productApplicationIds?.isEmpty() == false){
                tenant.permissions?.filter {
                    !productApplicationIds.contains(it.productApplicationId)
                }?.forEach {
                    it.enabled = false
                }
            } else {
                tenant.permissions?.forEach {
                    it.enabled = false
                }
            }


            tenant.permissions?.run {
                tenantPermissions += this
            }

            tenant.enabled = dtoTenant.enabled?:true
            tenant.permissions = tenantPermissions.toMutableList()

            tenantService.save(tenant)
        }

    }
}

