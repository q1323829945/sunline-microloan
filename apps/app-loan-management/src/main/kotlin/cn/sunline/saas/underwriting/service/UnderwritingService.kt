package cn.sunline.saas.underwriting.service

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.underwriting.event.UnderwritingBinding
import cn.sunline.saas.underwriting.exception.UnderwritingNotFound
import cn.sunline.saas.underwriting.invoke.UnderwritingInvoke
import cn.sunline.saas.underwriting.db.Underwriting
import cn.sunline.saas.underwriting.db.UnderwritingApplicationData
import cn.sunline.saas.underwriting.repository.UnderwritingRepository
import org.springframework.stereotype.Service


/**
 * @title: UnderwritingService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/7 14:26
 */
@Service
class UnderwritingService(
    private val underwritingRepository: UnderwritingRepository,
    private val underwritingInvoke: UnderwritingInvoke,
    private val underwritingBinding: UnderwritingBinding
) :
    BaseRepoService<Underwriting, Long>(underwritingRepository) {

    fun initiate(underwritingApplicationData: UnderwritingApplicationData) {
        save(
            Underwriting(
                underwritingApplicationData.applId,
                underwritingApplicationData.detail.customerId,
                underwritingApplicationData
            )
        )

        this.underwritingBinding.retrieveCustomerCreditRating(
            underwritingApplicationData.applId,
            underwritingInvoke.getPartnerIntegrated()?.customerCreditRatingPartner.toString(),
            underwritingApplicationData.detail.customerId
        )
    }

    fun updateCustomerCreditRating(applicationId: Long, customerCreditRating: String) {
        val underwriting = getOne(applicationId) ?: throw UnderwritingNotFound("underwriting data not found")
        underwriting.customerCreditRate = customerCreditRating
        save(underwriting)

        this.underwritingBinding.execCreditRisk(
            underwritingInvoke.getPartnerIntegrated()?.creditRiskPartner.toString(),
            underwriting
        )
    }

    fun updateCreditRisk(applicationId: Long,creditRisk:String){
        val underwriting = getOne(applicationId) ?: throw UnderwritingNotFound("underwriting data not found")
        underwriting.creditRisk = creditRisk
        save(underwriting)

        this.underwritingBinding.execRegulatoryCompliance(
            underwritingInvoke.getPartnerIntegrated()?.regulatoryCompliancePartner.toString(),
            underwriting
        )
    }

    fun updateRegulatoryCompliance(applicationId: Long,regulatoryCompliance:String){
        val underwriting = getOne(applicationId) ?: throw UnderwritingNotFound("underwriting data not found")
        underwriting.regulatoryCompliance = regulatoryCompliance
        save(underwriting)

        this.underwritingBinding.execFraudEvaluation(
            underwritingInvoke.getPartnerIntegrated()?.fraudEvaluationPartner.toString(),
            underwriting
        )
    }

    fun updateFraudEvaluation(applicationId: Long,fraudEvaluation:String){
        val underwriting = getOne(applicationId) ?: throw UnderwritingNotFound("underwriting data not found")
        underwriting.fraudEvaluation = fraudEvaluation
        save(underwriting)
    }


    fun getId(){
        val underwriting = underwritingInvoke.getPartnerIntegrated()
    }
}