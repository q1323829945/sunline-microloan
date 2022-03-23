package cn.sunline.saas.interest.component

import cn.sunline.saas.interest.model.dto.DTOInterestFeatureAdd
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @title: InterestProductFeatureService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 11:05
 */
@Component
class InterestProductFeatureComponent {
    @Autowired
    private lateinit var seq: Sequence

    fun register(
        productId: Long, interestFeatureData: DTOInterestFeatureAdd
    ): InterestProductFeature {

        val interestProductFeatureId = seq.nextId()
        val interestProductFeatureModality = InterestProductFeatureModality(
            id = interestProductFeatureId,
            baseYearDays = interestFeatureData.baseYearDays,
            adjustFrequency = interestFeatureData.adjustFrequency
        )
        val overdueInterestProductFeature = OverdueInterestProductFeatureModality(
            id = interestProductFeatureId,
            overdueInterestRatePercentage = interestFeatureData.overdueInterestRatePercentage
        )
        return InterestProductFeature(
            id = interestProductFeatureId,
            productId = productId,
            interestType = interestFeatureData.interestType,
            ratePlanId = interestFeatureData.ratePlanId,
            interest = interestProductFeatureModality,
            overdueInterest = overdueInterestProductFeature
        )
    }

}