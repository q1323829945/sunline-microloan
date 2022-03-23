package cn.sunline.saas.interest.arrangement.service

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.arrangement.factory.InterestArrangementFactory
import cn.sunline.saas.interest.arrangement.model.db.InterestArrangement
import cn.sunline.saas.interest.arrangement.model.dto.DTOInterestArrangementAdd
import cn.sunline.saas.interest.arrangement.repository.InterestArrangementRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: InterestArrangementService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/22 15:04
 */
@Service
class InterestArrangementService(private val interestArrangementRepo: InterestArrangementRepository) :
    BaseMultiTenantRepoService<InterestArrangement, Long>(interestArrangementRepo) {

    @Autowired
    private lateinit var interestArrangementFactory: InterestArrangementFactory

    fun registered(term: LoanTermType, dtoInterestArrangementAdd: DTOInterestArrangementAdd): InterestArrangement {
        return save(interestArrangementFactory.instance(term, dtoInterestArrangementAdd))
    }

}