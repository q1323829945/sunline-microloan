package cn.sunline.saas.interest.component

import cn.sunline.saas.interest.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import cn.sunline.saas.seq.Sequence
import java.math.BigDecimal
import java.nio.file.attribute.GroupPrincipal

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
        productId: Long,
        interestType: InterestType,
        ratePlanId: Long,
        baseYearDays: BaseYearDays,
        frequency: String,
        overdueInterestRatePercentage: Long
    ): InterestProductFeature {

        val interestProductFeatureId = seq.nextId()
        val interestProductFeatureModality = InterestProductFeatureModality(
            id = interestProductFeatureId, baseYearDays = baseYearDays, adjustFrequency = frequency
        )
        val overdueInterestProductFeature = OverdueInterestProductFeatureModality(
            id = interestProductFeatureId,
            overdueInterestRatePercentage = overdueInterestRatePercentage
        )
        return InterestProductFeature(
            id = interestProductFeatureId,
            productId = productId,
            interestType = interestType,
            ratePlanId = ratePlanId,
            interest = interestProductFeatureModality,
            overdueInterest = overdueInterestProductFeature
        )
    }

}