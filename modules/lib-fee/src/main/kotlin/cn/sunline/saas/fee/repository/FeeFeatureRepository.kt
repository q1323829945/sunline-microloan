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
    @Query(value = "select * from fee_feature where product_id = ?1", nativeQuery = true)
    fun getListByProductId(productId:Long):MutableList<FeeFeature>
}