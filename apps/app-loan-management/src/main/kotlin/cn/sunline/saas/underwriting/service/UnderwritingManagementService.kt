package cn.sunline.saas.underwriting.service

import cn.sunline.saas.global.constant.UnderwritingType
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.underwriting.db.Underwriting
import cn.sunline.saas.underwriting.event.UnderwritingPublish
import cn.sunline.saas.underwriting.exception.UnderwritingNotFound
import cn.sunline.saas.underwriting.exception.UnderwritingStatusCannotBeUpdate
import cn.sunline.saas.underwriting.repository.UnderwritingManagementRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class UnderwritingManagementService (private val underwritingManagementRepository: UnderwritingManagementRepository,
            private val underwritingPublish: UnderwritingPublish) :
    BaseMultiTenantRepoService<Underwriting, Long>(underwritingManagementRepository){

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

        return save(oldOne)
    }

    fun approval(id:Long){
        updateStatus(id,UnderwritingType.APPROVAL)
        underwritingPublish.customerOfferApproval(id)
        underwritingPublish.initiateLoanAgreement(id)
    }

    fun rejected(id:Long){
        updateStatus(id,UnderwritingType.REJECTED)
        underwritingPublish.customerOfferRejected(id)
    }

    private fun updateStatus( id:Long,underwritingType: UnderwritingType){
        val underwriting = getOne(id)?: throw UnderwritingNotFound("Invalid underwriting")
        if(underwriting.status != UnderwritingType.PENDING){
            throw UnderwritingStatusCannotBeUpdate("underwriting status cannot be update")
        }
        underwriting.status = underwritingType
        save(underwriting)
    }

}