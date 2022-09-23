package cn.sunline.saas.rbac.services

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.rbac.modules.Role
import cn.sunline.saas.rbac.repositories.RoleRepository
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class RoleService (val baseRepository: RoleRepository) : BaseMultiTenantRepoService<Role, Long>(baseRepository) {
    fun updateOne(oldRole: Role, newRole: Role): Role {
        oldRole.name = newRole.name
        oldRole.remark = newRole.remark
        oldRole.permissions = newRole.permissions
        return save(oldRole)
    }

    fun getByName(name: String): Role? {
        return baseRepository.findOne { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("name"), name))
            predicates.add(criteriaBuilder.equal(root.get<Long>("tenantId"),ContextUtil.getTenant().toLong()))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }.orElse(null)
    }
}