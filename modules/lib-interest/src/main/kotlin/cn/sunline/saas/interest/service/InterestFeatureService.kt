package cn.sunline.saas.interest.service

import cn.sunline.saas.interest.factory.InterestFeatureFactory
import cn.sunline.saas.interest.model.db.InterestFeature
import cn.sunline.saas.interest.model.dto.DTOInterestFeatureAdd
import cn.sunline.saas.interest.repository.InterestFeatureRepository
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

    fun getOneByProductId(productId:Long):InterestFeature?{
        return interestFeatureRepository.getOneByProductId(productId)
    }
}