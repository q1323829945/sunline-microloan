package cn.sunline.saas.underwriting.service

import cn.sunline.saas.customer.offer.exceptions.CustomerOfferNotFoundException
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.global.constant.UnderwritingType
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.rpc.pubsub.StatisticsPublish
import cn.sunline.saas.rpc.pubsub.dto.DTOCommissionDetail
import cn.sunline.saas.rpc.pubsub.dto.DTOLoanApplicationDetail
import cn.sunline.saas.underwriting.db.Underwriting
import cn.sunline.saas.underwriting.event.UnderwritingPublish
import cn.sunline.saas.underwriting.exception.UnderwritingCannotBeUpdate
import cn.sunline.saas.underwriting.exception.UnderwritingNotFound
import cn.sunline.saas.underwriting.exception.UnderwritingStatusCannotBeUpdate
import cn.sunline.saas.underwriting.repository.UnderwritingManagementRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UnderwritingManagementService (
    private val underwritingManagementRepository: UnderwritingManagementRepository,
    private val underwritingPublish: UnderwritingPublish,
    private val statisticsPublish: StatisticsPublish
) : BaseMultiTenantRepoService<Underwriting, Long>(underwritingManagementRepository){

    @Autowired
    private lateinit var customerOfferService: CustomerOfferService

    @Autowired
    private lateinit var customerLoanApplyService: CustomerLoanApplyService

    fun getPaged(name:String?,pageable: Pageable):Page<Underwriting>{
        val page = getPageWithTenant(null, Pageable.unpaged())

        val newPageContent = mutableListOf<Underwriting>()
        name?.run {
            page.content.forEach {
                if(it.applicationData.detail.name.contains(name)){
                    newPageContent.add(it)
                }
            }
        }?:run {
            newPageContent.addAll(page.content)
        }
        return rePaged(newPageContent,pageable)
    }


    fun update(oldOne:Underwriting,newOne:Underwriting):Underwriting{
        oldOne.fraudEvaluation = newOne.fraudEvaluation
        oldOne.creditRisk = newOne.creditRisk
        oldOne.customerCreditRate = newOne.customerCreditRate
        oldOne.regulatoryCompliance = newOne.regulatoryCompliance

        if(oldOne.status != UnderwritingType.PENDING){
            throw UnderwritingCannotBeUpdate("underwriting cannot be update")
        }

        if(!oldOne.fraudEvaluation.isNullOrEmpty() && !oldOne.creditRisk.isNullOrEmpty()
            && !oldOne.customerCreditRate.isNullOrEmpty() && !oldOne.regulatoryCompliance.isNullOrEmpty()){
            approval(oldOne.id)
        }

        return save(oldOne)
    }

    fun approval(id:Long){
        updateStatus(id,UnderwritingType.APPROVAL)
        underwritingPublish.customerOfferApproval(id)
        underwritingPublish.initiateLoanAgreement(id)
        syncLoanApplicationStatistics(id)
    }

    fun rejected(id:Long){
        updateStatus(id,UnderwritingType.REJECTED)
        underwritingPublish.customerOfferRejected(id)
        syncLoanApplicationStatistics(id)
    }

    private fun updateStatus( id:Long,underwritingType: UnderwritingType){
        val underwriting = getOne(id)?: throw UnderwritingNotFound("Invalid underwriting")
        if(underwriting.status != UnderwritingType.PENDING){
            throw UnderwritingStatusCannotBeUpdate("underwriting status cannot be update")
        }
        underwriting.status = underwritingType
        save(underwriting)
    }

    private fun syncLoanApplicationStatistics(customerOfferId: Long) {
        val customerOfferLoan = customerLoanApplyService.retrieve(customerOfferId)
        val customerOffer = customerOfferService.getOne(customerOfferId)
            ?: throw CustomerOfferNotFoundException("Invalid customer offer")
        statisticsPublish.addLoanApplicationDetail(
            DTOLoanApplicationDetail(
                channel = "0",
                productId = customerOffer.productId,
                productName = customerOffer.productName,
                applicationId = customerOfferId,
                amount = customerOfferLoan.loan!!.amount.toBigDecimal(),
                currency = customerOfferLoan.loan!!.currency,
                status = customerOffer.status,
            )
        )
        statisticsPublish.addLoanApplicationStatistics()

        statisticsPublish.addCommissionDetail(
            DTOCommissionDetail(
                channel = "0",
                applicationId = customerOfferId,
                amount = customerOfferLoan.loan!!.amount.toBigDecimal(),
                currency = customerOfferLoan.loan!!.currency,
                status = customerOffer.status,
            )
        )

        statisticsPublish.addCommissionStatistics()
    }
}