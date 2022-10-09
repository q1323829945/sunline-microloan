package cn.sunline.saas.channel.interest.service


import cn.sunline.saas.channel.interest.factory.InterestFeatureFactory
import cn.sunline.saas.channel.interest.model.db.InterestFeature
import cn.sunline.saas.channel.interest.model.dto.DTOInterestFeatureAdd
import cn.sunline.saas.channel.interest.repository.InterestFeatureRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: InterestProductFeatureService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/10 16:21
 */
@Service
class InterestFeatureService(private val interestFeatureRepository: InterestFeatureRepository) :
    BaseMultiTenantRepoService<InterestFeature, Long>(interestFeatureRepository) {

    @Autowired
    private lateinit var interestFeatureFactory: InterestFeatureFactory

    fun register(productId: Long, interestFeatureData: DTOInterestFeatureAdd): InterestFeature {
        return save(interestFeatureFactory.instance(productId, interestFeatureData))
    }

    fun findByProductId(productId:Long):InterestFeature? {
        return interestFeatureRepository.findByProductId(productId)
    }

    fun findByRatePlanId(ratePlanId:Long): MutableList<InterestFeature>?{
        return interestFeatureRepository.findByRatePlanId(ratePlanId)
    }
}