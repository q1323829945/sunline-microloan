package cn.sunline.saas.channel.rbac.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.channel.rbac.modules.User

interface UserRepository : BaseRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun findByPersonId(personId:Long): User?
}