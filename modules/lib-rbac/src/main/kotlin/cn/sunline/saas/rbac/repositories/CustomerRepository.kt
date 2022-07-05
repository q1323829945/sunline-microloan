package cn.sunline.saas.rbac.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.rbac.modules.Customer

interface CustomerRepository : BaseRepository<Customer, Long> {
    fun findByUserId(userId:String):Customer?
}