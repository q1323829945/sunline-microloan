package cn.sunline.saas.customer_offer.service

import cn.sunline.saas.customer.offer.modules.ApplyStatus.*
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.customer_offer.controllers.model.OperationType
import cn.sunline.saas.customer_offer.controllers.model.OperationType.*
import cn.sunline.saas.customer_offer.exceptions.CustomerOfferNotFoundException
import cn.sunline.saas.customer_offer.exceptions.CustomerOfferStatusException
import cn.sunline.saas.customer_offer.service.dto.DTOCustomerOfferPage
import cn.sunline.saas.customer_offer.service.dto.DTOManagementCustomerOfferView
import cn.sunline.saas.party.organisation.service.OrganisationService
import org.joda.time.DateTimeZone
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.function.Function

@Service
class AppCustomerOfferService {

    @Autowired
    private lateinit var customerOfferService: CustomerOfferService

    @Autowired
    private lateinit var customerLoanApplyService: CustomerLoanApplyService

    @Autowired
    private lateinit var organisationService: OrganisationService

    fun getPaged(customerId:Long?,productId:Long?,productName:String?,pageable: Pageable):Page<DTOCustomerOfferPage> {
        val page = customerOfferService.getCustomerOfferPaged(customerId,productId,productName, pageable).map {
            val apply = customerLoanApplyService.getOne(it.id!!)
            val organisation = organisationService.getOrganisationByPartyId(it.customerId)
            DTOCustomerOfferPage(
                it.id!!,
                organisation?.legalEntityIndicator,
                organisation?.organisationSector,
                apply?.amount?.toString(),
                it.datetime.toDateTime(DateTimeZone.getDefault()).toString("yyyy-MM-dd HH:mm:ss"),
                it.productName,
                it.status
            )
        }

        return page
    }

    fun updateStatus(operationType: OperationType, id: Long){
        val customerOffer = customerOfferService.getOne(id)?:throw CustomerOfferNotFoundException("Invalid customer offer")
        when(operationType){
            PASS -> {
                when(customerOffer.status){
                    SUBMIT -> customerOffer.status = APPROVALED
                    APPROVALED -> customerOffer.status = LOAN
                    LOAN -> customerOffer.status = FINISH
                    else -> throw CustomerOfferStatusException("status can not be update")
                }
            }
            REJECT -> when(customerOffer.status){
                SUBMIT -> customerOffer.status = REJECTED
                else -> throw CustomerOfferStatusException("status can not be update")
            }
        }

        customerOfferService.save(customerOffer)
    }

    fun getDetail(id: Long): DTOManagementCustomerOfferView {
        val customerOffer = customerOfferService.getOne(id)?:throw CustomerOfferNotFoundException("Invalid customer offer")
        val organisation = organisationService.getOrganisationByPartyId(customerOffer.customerId)
        val customerOfferLoan = customerLoanApplyService.retrieve(id)


        return DTOManagementCustomerOfferView(
            organisation = organisation?.run { organisationService.getDTOOrganisationView(organisation) },
            customerOfferLoan = customerOfferLoan
        )
    }


}