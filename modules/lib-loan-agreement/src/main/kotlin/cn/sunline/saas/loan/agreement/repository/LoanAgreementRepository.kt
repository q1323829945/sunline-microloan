package cn.sunline.saas.loan.agreement.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.loan.agreement.model.db.LoanAgreement

/**
 * @title: LoanAgreementRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/1 11:20
 */
interface LoanAgreementRepository : BaseRepository<LoanAgreement, Long>{
    fun findByApplicationId(applicationId:Long):LoanAgreement?
}