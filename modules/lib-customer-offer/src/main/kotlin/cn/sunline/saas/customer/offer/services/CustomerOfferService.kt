package cn.sunline.saas.customer.offer.services

import cn.sunline.saas.customer.offer.modules.ApplyStatus
import cn.sunline.saas.customer.offer.modules.db.CustomerOffer
import cn.sunline.saas.customer.offer.modules.dto.CustomerOfferProcedureView
import cn.sunline.saas.customer.offer.modules.dto.DTOCustomerOfferAdd
import cn.sunline.saas.customer.offer.modules.dto.DTOCustomerOfferPage
import cn.sunline.saas.customer.offer.repositories.CustomerOfferRepository
import cn.sunline.saas.exceptions.ManagementExceptionCode
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
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant

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
                dtoCustomerOffer.product.productId,
                ApplyStatus.RECORD,
                data,
                Instant.now(Clock.systemUTC()).toEpochMilli().toString()
        ))

        val customerOfferProcedureView = objectMapper.convertValue<CustomerOfferProcedureView>(dtoCustomerOffer.customerOfferProcedure)

        customerOfferProcedureView.customerOfferId = save.id!!

        return customerOfferProcedureView

    }


    fun getOneById(id: Long): CustomerOffer {
        return getOne(id) ?: throw NotFoundException("Invalid customer offer", ManagementExceptionCode.DATA_NOT_FOUND)
    }


    fun getCustomerOfferPaged(customerId:Long,pageable: Pageable): Page<DTOCustomerOfferPage>{

        val page = customerOfferRepo.getCustomerOfferPaged(customerId,pageable).map {
            DTOCustomerOfferPage(
                it["customerOfferId"].toString().toLong(),
                it["amount"]?.run { BigDecimal(it["amount"].toString()) },
                it["datetime"].toString(),
                it["productName"].toString(),
                ApplyStatus.valueOf(it["status"].toString())
            )
        }
        return page
    }
}
