package cn.sunline.saas.loan.product.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.global.constant.BankingProductStatus
import cn.sunline.saas.loan.product.model.db.LoanProduct
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @title: LoanProductRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 15:19
 */
interface LoanProductRepository:BaseRepository<LoanProduct,Long>{
}