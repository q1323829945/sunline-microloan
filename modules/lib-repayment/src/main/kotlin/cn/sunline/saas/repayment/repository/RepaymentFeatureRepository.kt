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
    fun findByProductId(productId:Long):RepaymentFeature?
}