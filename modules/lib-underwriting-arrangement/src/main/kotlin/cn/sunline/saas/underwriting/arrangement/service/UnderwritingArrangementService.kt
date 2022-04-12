package cn.sunline.saas.underwriting.arrangement.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.underwriting.arrangement.factory.UnderwritingArrangementFactory
import cn.sunline.saas.underwriting.arrangement.model.db.UnderwritingArrangement
import cn.sunline.saas.underwriting.arrangement.model.dto.DTOUnderwritingArrangementAdd
import cn.sunline.saas.underwriting.arrangement.repository.UnderwritingArrangementRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: UnderwritingArrangementService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/1 11:21
 */
@Service
class UnderwritingArrangementService(private val underwritingArrangementRepo: UnderwritingArrangementRepository) :
    BaseMultiTenantRepoService<UnderwritingArrangement, Long>(underwritingArrangementRepo) {

    @Autowired
    private lateinit var underwritingArrangementFactory: UnderwritingArrangementFactory

    fun registered(dtoUnderwritingArrangementAdd: DTOUnderwritingArrangementAdd): MutableList<UnderwritingArrangement> {
        return save(underwritingArrangementFactory.instance(dtoUnderwritingArrangementAdd).asIterable()).toMutableList()
    }

}