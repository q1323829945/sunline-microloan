package cn.sunline.saas.interest.service

import cn.sunline.saas.interest.factory.InterestFeatureFactory
import cn.sunline.saas.interest.model.db.InterestFeature
import cn.sunline.saas.interest.model.dto.DTOInterestFeatureAdd
import cn.sunline.saas.interest.repository.InterestFeatureRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

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
        return getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("productId"), productId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }

    fun findByRatePlanId(ratePlanId: Long): List<InterestFeature>? {
        return getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("ratePlanId"), ratePlanId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged()).content
    }
}