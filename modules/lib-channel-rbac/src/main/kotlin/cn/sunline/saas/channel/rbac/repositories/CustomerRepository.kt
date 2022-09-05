package cn.sunline.saas.channel.rbac.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.channel.rbac.modules.Customer

interface CustomerRepository : BaseRepository<Customer, Long> {
    fun findByUserId(userId:String): Customer?
}