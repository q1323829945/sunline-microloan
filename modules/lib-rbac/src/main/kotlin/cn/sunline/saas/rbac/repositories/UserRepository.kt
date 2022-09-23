package cn.sunline.saas.rbac.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.rbac.modules.User

interface UserRepository : BaseRepository<User, Long> {
    fun findByUsernameAndTenantId(username: String,tenantId:Long): User?
    fun findByPersonIdAndTenantId(personId:Long,tenantId:Long):User?
}