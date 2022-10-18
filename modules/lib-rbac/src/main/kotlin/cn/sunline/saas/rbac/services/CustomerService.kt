package cn.sunline.saas.rbac.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.rbac.exception.CustomerNotFoundException
import cn.sunline.saas.rbac.modules.Customer
import cn.sunline.saas.rbac.modules.dto.DTOCustomerAdd
import cn.sunline.saas.rbac.modules.dto.DTOCustomerChange
import cn.sunline.saas.rbac.repositories.CustomerRepository
import cn.sunline.saas.seq.Sequence
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

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
        getOneWithTenant{ root,_,c ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(c.equal(root.get<String>("userId"),dtoCustomerAdd.userId))
            c.and(*(predicates.toTypedArray()))
        }?.run {
            this
        }

        return save(
            Customer(
                sequence.nextId(),
                dtoCustomerAdd.username,
                dtoCustomerAdd.userId
            )
        )
    }

    fun updateOne(id:Long,dtoCustomerChange: DTOCustomerChange):Customer{
        val oldOne = getOne(id)?: throw CustomerNotFoundException("Invalid customer")
        oldOne.username = dtoCustomerChange.username
        return save(oldOne)
    }

}