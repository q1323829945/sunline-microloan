package cn.sunline.saas.customer.offer.services

import cn.sunline.saas.customer.offer.modules.ApplyStatus
import cn.sunline.saas.customer.offer.modules.db.CustomerOffer
import cn.sunline.saas.customer.offer.modules.dto.CustomerOfferProcedureView
import cn.sunline.saas.customer.offer.modules.dto.DTOCustomerOfferAdd
import cn.sunline.saas.customer.offer.modules.dto.DTOCustomerOfferPage
import cn.sunline.saas.customer.offer.repositories.CustomerOfferRepository
import cn.sunline.saas.exceptions.NotFoundException
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Instant
import javax.persistence.criteria.Predicate

@Service
class CustomerOfferService (private val customerOfferRepo: CustomerOfferRepository) :
        BaseMultiTenantRepoService<CustomerOffer, Long>(customerOfferRepo){

    @Autowired
    private lateinit var seq: Sequence

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun initiate(dtoCustomerOffer: DTOCustomerOfferAdd): CustomerOfferProcedureView {
        val data = Gson().toJson(dtoCustomerOffer)

        val save = this.save(CustomerOffer(
                seq.nextId(),
                dtoCustomerOffer.customerOfferProcedure.customerId,
                ApplyStatus.RECORD,
                data,
                Instant.now(Clock.systemUTC()).toEpochMilli().toString()
        ))

        val customerOfferProcedureView = objectMapper.convertValue<CustomerOfferProcedureView>(dtoCustomerOffer.customerOfferProcedure)

        customerOfferProcedureView.customerOfferId = save.id!!

        return customerOfferProcedureView

    }

    fun findProductIdById(id:Long):Long{
        val customerOffer = this.getOne(id)?:throw NotFoundException("Invalid customer offer")

        val dtoCustomerOffer = Gson().fromJson(customerOffer.data,DTOCustomerOfferAdd::class.java)

        return dtoCustomerOffer.product.productId
    }


    fun getCustomerOfferPaged(customerId:Long,pageable: Pageable): Page<DTOCustomerOfferPage>{
        val page = this.getPaged(
                {root, _, criteriaBuilder ->
                    val predicates = mutableListOf<Predicate>()
                    predicates.add(criteriaBuilder.equal(root.get<Long>("customerId"),customerId))
                    criteriaBuilder.and(*(predicates.toTypedArray()))
                },pageable)
                .map {
                    val dto = objectMapper.convertValue<DTOCustomerOfferPage>(it)
                    dto.customerOfferId = it.id!!
                    dto
                }

        return page
    }
}