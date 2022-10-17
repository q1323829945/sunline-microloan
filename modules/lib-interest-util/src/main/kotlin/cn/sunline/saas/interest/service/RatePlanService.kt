package cn.sunline.saas.interest.service

import cn.sunline.saas.interest.model.RatePlan
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.interest.repository.RatePlanRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

/**
 * @title: RatePlanService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 11:03
 */
@Service
class RatePlanService(private val ratePlanRepository: RatePlanRepository) :
    BaseMultiTenantRepoService<RatePlan, Long>(ratePlanRepository) {

    @Autowired
    private lateinit var snowflakeService: Sequence

    fun addOne(ratePlan: RatePlan): RatePlan {
        ratePlan.id = snowflakeService.nextId()
        return save(ratePlan)
    }

    fun updateOne(oldRatePlan: RatePlan, newRatePlan: RatePlan): RatePlan {
        oldRatePlan.name = newRatePlan.name
        oldRatePlan.rates = newRatePlan.rates
        return save(oldRatePlan)
    }

    fun findByType(type: RatePlanType): RatePlan? {
        return getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<RatePlanType>("type"), type))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }


    fun getRatePlanPageByType(type: RatePlanType?): Page<RatePlan> {
        return getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            type?.let { predicates.add(criteriaBuilder.equal(root.get<RatePlanType>("type"), it)) }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
    }

    fun getAllCustomRatePlanPage(): Page<RatePlan> {
        return getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.notEqual(root.get<RatePlanType>("type"), RatePlanType.STANDARD))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
    }
}