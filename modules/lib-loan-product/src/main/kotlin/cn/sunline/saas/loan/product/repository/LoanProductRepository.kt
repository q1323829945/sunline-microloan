package cn.sunline.saas.loan.product.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.loan.product.model.db.LoanProduct

/**
 * @title: LoanProductRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 15:19
 */
interface LoanProductRepository:BaseRepository<LoanProduct,Long>{
    fun findByIdentificationCode(identificationCode:String):LoanProduct?
}