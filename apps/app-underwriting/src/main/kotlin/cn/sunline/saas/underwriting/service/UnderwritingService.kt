package cn.sunline.saas.underwriting.service

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.underwriting.event.UnderwritingPublish
import cn.sunline.saas.underwriting.exception.UnderwritingNotFound
import cn.sunline.saas.underwriting.invoke.UnderwritingInvoke
import cn.sunline.saas.underwriting.model.db.Underwriting
import cn.sunline.saas.underwriting.model.db.UnderwritingApplicationData
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
    private val underwritingPublish: UnderwritingPublish
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

        this.underwritingPublish.retrieveCustomerCreditRating(
            underwritingApplicationData.applId,
            underwritingInvoke.getPartnerIntegrated()?.customerCreditRatingPartner.toString(),
            underwritingApplicationData.detail.customerId
        )
    }

    fun updateCustomerCreditRating(applicationId: Long, customerCreditRating: String) {
        val underwriting = getOne(applicationId) ?: throw UnderwritingNotFound("underwriting data not found")
        underwriting.customerCreditRate = customerCreditRating
        save(underwriting)

        this.underwritingPublish.execCreditRisk(
            underwritingInvoke.getPartnerIntegrated()?.creditRiskPartner.toString(),
            underwriting
        )
    }
}