package cn.sunline.saas.rbac.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.rbac.modules.User

interface UserRepository : BaseRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun findByPersonId(personId:Long):User?
}