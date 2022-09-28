package cn.sunline.saas.interest.factory

import cn.sunline.saas.interest.model.db.InterestFeature
import cn.sunline.saas.interest.model.db.InterestFeatureModality
import cn.sunline.saas.interest.model.db.OverdueInterestFeatureModality
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
class InterestFeatureFactory {
    @Autowired
    private lateinit var seq: Sequence

    fun instance(
        productId: Long, interestFeatureData: DTOInterestFeatureAdd
    ): InterestFeature {

        val interestFeatureId = seq.nextId()
        val interestFeatureModality = InterestFeatureModality(
            id = interestFeatureId,
            baseYearDays = interestFeatureData.baseYearDays,
            adjustFrequency = interestFeatureData.adjustFrequency,
            basicPoint = interestFeatureData.basicPoint
        )
        val overdueInterestProductFeature = OverdueInterestFeatureModality(
            id = interestFeatureId,
            overdueInterestRatePercentage = interestFeatureData.overdueInterestRatePercentage
        )
        return InterestFeature(
            id = interestFeatureId,
            productId = productId,
            interestType = interestFeatureData.interestType,
            ratePlanId = interestFeatureData.ratePlanId,
            interest = interestFeatureModality,
            overdueInterest = overdueInterestProductFeature
        )
    }

}