package cn.sunline.saas.tenant.service.dto

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.multi_tenant.model.TenantPermission

data class DTOTenantView(
    val id:Long,
    val enabled:Boolean,
    val country: CountryType,
    val permissions:List<TenantPermission>
)