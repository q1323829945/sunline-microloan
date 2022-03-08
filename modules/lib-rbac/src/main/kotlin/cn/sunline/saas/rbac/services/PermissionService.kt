package cn.sunline.saas.rbac.services

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.rbac.modules.Permission
import cn.sunline.saas.rbac.repositories.PermissionRepository
import org.springframework.stereotype.Service

@Service
class PermissionService (baseRepository: PermissionRepository) : BaseRepoService<Permission, Long>(baseRepository) {
}