package cn.sunline.saas.repayment.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.repayment.model.db.RepaymentFeature
import org.springframework.data.jpa.repository.Query

/**
 * @title: RepaymentProductFeatureRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/11 14:33
 */
interface RepaymentFeatureRepository:BaseRepository<RepaymentFeature,Long>{
    @Query(value = "select * from repayment_feature where product_id = ?1", nativeQuery = true)
    fun getOneByProductId(productId:Long):RepaymentFeature?
}