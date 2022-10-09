package cn.sunline.saas.channel.interest.service

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.RatePlan
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.interest.repository.RatePlanRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
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
}