package cn.sunline.saas.interest.service

import cn.sunline.saas.interest.model.InterestProductFeature
import cn.sunline.saas.interest.model.InterestProductFeatureModality
import cn.sunline.saas.interest.model.InterestType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import cn.sunline.saas.seq.Sequence

/**
 * @title: InterestProductFeatureService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 11:05
 */
@Component
class InterestProductFeatureService {
    @Autowired
    private lateinit var seq: Sequence

    fun register(productId:Long,interestType: InterestType, ratePlanId: Long, frequency: String, calculationMethod: String): InterestProductFeature {

        val interestProductFeatureId = seq.nextId()
        val interestProductFeatureModality = InterestProductFeatureModality(
            interestProductFeatureId = interestProductFeatureId,
            calculationMethod = calculationMethod,
            frequency = frequency
        )
        return InterestProductFeature(
            id = interestProductFeatureId,
            productId = productId,
            interestType = interestType,
            ratePlanId = ratePlanId,
            modality = interestProductFeatureModality
        )
    }
}