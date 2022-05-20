package cn.sunline.saas.customer.offer.services

import cn.sunline.saas.customer.offer.modules.ApplyStatus
import cn.sunline.saas.customer.offer.modules.db.CustomerOffer
import cn.sunline.saas.customer.offer.modules.dto.CustomerOfferProcedureView
import cn.sunline.saas.customer.offer.modules.dto.DTOCustomerOfferAdd
import cn.sunline.saas.customer.offer.repositories.CustomerOfferRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import org.joda.time.Instant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class CustomerOfferService (private val customerOfferRepo: CustomerOfferRepository) :
        BaseMultiTenantRepoService<CustomerOffer, Long>(customerOfferRepo){

    @Autowired
    private lateinit var seq: Sequence

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun initiate(dtoCustomerOffer: DTOCustomerOfferAdd): CustomerOfferProcedureView {

        val data = objectMapper.valueToTree<JsonNode>(dtoCustomerOffer).toPrettyString()

        val save = this.save(CustomerOffer(
                seq.nextId(),
                dtoCustomerOffer.customerOfferProcedure.customerId,
                dtoCustomerOffer.product.productId,
                dtoCustomerOffer.product.productName,
                ApplyStatus.RECORD,
                data,
                Instant.now()
        ))

        val customerOfferProcedureView = objectMapper.convertValue<CustomerOfferProcedureView>(dtoCustomerOffer.customerOfferProcedure)

        customerOfferProcedureView.customerOfferId = save.id.toString()

        return customerOfferProcedureView

    }


    fun getOneById(id: Long): CustomerOffer? {
        return this.getOne(id)
    }

    fun getCustomerOfferPaged(customerId:Long?,productId:Long?,productName:String?,pageable: Pageable): Page<CustomerOffer>{
        val page = getPageWithTenant({root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            customerId?.run { predicates.add(criteriaBuilder.equal(root.get<Long>("customerId"),customerId)) }
            productId?.run { predicates.add(criteriaBuilder.equal(root.get<Long>("productId"),productId)) }
            productName?.run { predicates.add(criteriaBuilder.like(root.get("productName"),"$productName%")) }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        },pageable)
        return page
    }
}
