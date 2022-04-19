package cn.sunline.saas.disbursement.arrangement.service

import cn.sunline.saas.disbursement.arrangement.factory.DisbursementArrangementFactory
import cn.sunline.saas.disbursement.arrangement.model.db.DisbursementArrangement
import cn.sunline.saas.disbursement.arrangement.model.dto.DTODisbursementArrangementAdd
import cn.sunline.saas.disbursement.arrangement.repository.DisbursementArrangementRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: DisbursementArrangementService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/18 16:00
 */
@Service
class DisbursementArrangementService(private val disbursementArrangementRepository: DisbursementArrangementRepository) :
    BaseMultiTenantRepoService<DisbursementArrangement, Long>(disbursementArrangementRepository) {

    @Autowired
    private lateinit var disbursementArrangementFactory: DisbursementArrangementFactory

    fun registered(
        agreementId: Long,
        disbursementArrangementAdd: DTODisbursementArrangementAdd
    ): MutableList<DisbursementArrangement> {
        return save(disbursementArrangementFactory.instance(agreementId, disbursementArrangementAdd)).toMutableList()
    }
}