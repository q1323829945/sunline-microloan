package cn.sunline.saas.underwriting.service

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.underwriting.event.UnderwritingPublish
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

    fun initiate(dtoUnderwritingAdd: UnderwritingApplicationData): Unit {
        //TODO save underwriting data

        this.underwritingPublish.retrieveCustomerCreditRating(
            underwritingInvoke.getPartnerIntegrated()?.customerCreditRatingPartner.toString(),
            dtoUnderwritingAdd.detail.customerId
        )
    }
}