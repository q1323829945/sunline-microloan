package cn.sunline.saas.channel.rbac.services

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.channel.rbac.modules.Role
import cn.sunline.saas.channel.rbac.repositories.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService (val baseRepository: RoleRepository) : BaseRepoService<Role, Long>(baseRepository) {
    fun updateOne(oldRole: Role, newRole: Role): Role {
        oldRole.name = newRole.name
        oldRole.remark = newRole.remark
        oldRole.permissions = newRole.permissions
        return save(oldRole)
    }

    fun getByName(name: String): Role? {
        return baseRepository.findOne { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<String>("name"), name)
        }.orElse(null)
    }
}