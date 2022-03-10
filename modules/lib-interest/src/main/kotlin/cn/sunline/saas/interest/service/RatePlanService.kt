package cn.sunline.saas.interest.service

import cn.sunline.saas.interest.model.RatePlan
import cn.sunline.saas.interest.repository.RatePlanRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.seq.snowflake.services.SnowflakeService

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

    fun addOne(ratePlan: RatePlan):RatePlan{
        ratePlan.id = snowflakeService.nextId()
        return save(ratePlan)
    }

    fun updateOne(oldRatePlan: RatePlan, newRatePlan: RatePlan): RatePlan {
        oldRatePlan.name = newRatePlan.name
        oldRatePlan.rates = newRatePlan.rates
        return save(oldRatePlan)
    }
}