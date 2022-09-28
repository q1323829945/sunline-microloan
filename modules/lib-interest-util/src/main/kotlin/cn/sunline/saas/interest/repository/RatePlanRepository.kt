package cn.sunline.saas.interest.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.interest.model.RatePlan
import cn.sunline.saas.interest.model.RatePlanType

/**
 * @title: RatePlanRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 11:02
 */
interface RatePlanRepository : BaseRepository<RatePlan, Long>{
    fun findByTypeAndTenantId(type: RatePlanType,tenantId:Long): RatePlan?
}