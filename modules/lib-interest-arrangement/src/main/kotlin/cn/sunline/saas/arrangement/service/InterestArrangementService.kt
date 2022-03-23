package cn.sunline.saas.arrangement.service

import cn.sunline.saas.arrangement.model.db.InterestArrangement
import cn.sunline.saas.arrangement.repository.InterestArrangementRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService

/**
 * @title: InterestArrangementService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/22 15:04
 */
class InterestArrangementService(private val interestArrangementRepo: InterestArrangementRepository) :
    BaseMultiTenantRepoService<InterestArrangement, Long>(interestArrangementRepo) {
}