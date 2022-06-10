package cn.sunline.saas.multi_tenant.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.multi_tenant.model.TenantPermission

interface TenantPermissionRepository : BaseRepository<TenantPermission, Long>