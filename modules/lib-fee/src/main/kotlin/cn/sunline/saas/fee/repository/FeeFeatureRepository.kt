package cn.sunline.saas.fee.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.fee.model.db.FeeFeature
import org.springframework.data.jpa.repository.Query

/**
 * @title: FeeRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/14 10:35
 */
interface FeeFeatureRepository : BaseRepository<FeeFeature, Long>{
    fun findByProductId(productId:Long):MutableList<FeeFeature>?
}