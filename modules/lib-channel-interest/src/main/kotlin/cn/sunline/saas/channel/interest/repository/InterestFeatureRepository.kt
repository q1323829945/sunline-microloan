package cn.sunline.saas.channel.interest.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.channel.interest.model.db.InterestFeature
import org.springframework.data.jpa.repository.Query

/**
 * @title: InterestProductFeatureRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/10 16:20
 */
interface InterestFeatureRepository : BaseRepository<InterestFeature, Long>{

    fun findByProductId(productId:Long):InterestFeature?

    fun findByRatePlanId(ratePlanId:Long): MutableList<InterestFeature>?
}