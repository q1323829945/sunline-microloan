package cn.sunline.saas.channel.interest.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.channel.interest.model.RatePlan
import cn.sunline.saas.channel.interest.model.RatePlanType

/**
 * @title: RatePlanRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 11:02
 */
interface RatePlanRepository : BaseRepository<RatePlan, Long>{
    fun findByType(type: RatePlanType): RatePlan?
}