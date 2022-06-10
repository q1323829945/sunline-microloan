package cn.sunline.saas.multi_tenant.services

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.multi_tenant.model.TenantPermission
import cn.sunline.saas.multi_tenant.repository.TenantPermissionRepository
import org.springframework.stereotype.Service

@Service
class TenantPermissionService(private val tenantPermissionRepository: TenantPermissionRepository) : BaseRepoService<TenantPermission, Long>(tenantPermissionRepository) {
}