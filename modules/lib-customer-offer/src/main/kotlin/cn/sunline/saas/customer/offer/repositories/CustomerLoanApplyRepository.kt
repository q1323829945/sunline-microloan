package cn.sunline.saas.customer.offer.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.customer.offer.modules.db.CustomerLoanApply

interface CustomerLoanApplyRepository: BaseRepository<CustomerLoanApply, Long>{
    fun findByCustomerOfferId(customerOfferId:Long):CustomerLoanApply?
}