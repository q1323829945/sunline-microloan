package cn.sunline.saas.channel.rbac.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.channel.rbac.exception.CustomerNotFoundException
import cn.sunline.saas.channel.rbac.modules.Customer
import cn.sunline.saas.channel.rbac.modules.dto.DTOCustomerAdd
import cn.sunline.saas.channel.rbac.modules.dto.DTOCustomerChange
import cn.sunline.saas.channel.rbac.repositories.CustomerRepository
import cn.sunline.saas.seq.Sequence
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val sequence: Sequence
    )
    : BaseMultiTenantRepoService<Customer,Long>(customerRepository) {

    fun findByUserId(userId:String): Customer?{
        return customerRepository.findByUserId(userId)
    }


    fun addOne(dtoCustomerAdd: DTOCustomerAdd): Customer {
        return save(
            Customer(
                sequence.nextId(),
                dtoCustomerAdd.username,
                dtoCustomerAdd.userId
            )
        )
    }

    fun updateOne(id:Long,dtoCustomerChange: DTOCustomerChange): Customer {
        val oldOne = getOne(id)?: throw CustomerNotFoundException("Invalid customer")
        oldOne.username = dtoCustomerChange.username
        return save(oldOne)
    }

}