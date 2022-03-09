package cn.sunline.saas.interest.service

import cn.sunline.saas.interest.model.RatePlan
import cn.sunline.saas.interest.repository.RatePlanRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service

/**
 * @title: RatePlanService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 11:03
 */
@Service
class RatePlanService(private val ratePlanRepository: RatePlanRepository) :
    BaseMultiTenantRepoService<RatePlan, Long>(ratePlanRepository) {

    fun updateOne(oldRatePlan: RatePlan, newRatePlan: RatePlan): RatePlan {
        oldRatePlan.name = newRatePlan.name
        oldRatePlan.rates = newRatePlan.rates
        return save(oldRatePlan)
    }
}