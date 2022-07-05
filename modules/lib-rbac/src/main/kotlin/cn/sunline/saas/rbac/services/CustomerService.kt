package cn.sunline.saas.rbac.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.rbac.modules.Customer
import cn.sunline.saas.rbac.modules.dto.DTOCustomerAdd
import cn.sunline.saas.rbac.repositories.CustomerRepository
import cn.sunline.saas.seq.Sequence
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val sequence: Sequence
    )
    : BaseMultiTenantRepoService<Customer,Long>(customerRepository) {

    fun findByUserId(userId:String):Customer?{
        return customerRepository.findByUserId(userId)
    }


    fun addOne(dtoCustomerAdd: DTOCustomerAdd):Customer{
        return save(
            Customer(
                sequence.nextId(),
                dtoCustomerAdd.username,
                dtoCustomerAdd.userId
            )
        )
    }

}