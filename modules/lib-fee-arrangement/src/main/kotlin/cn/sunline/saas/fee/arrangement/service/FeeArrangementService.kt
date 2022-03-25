package cn.sunline.saas.fee.arrangement.service

import cn.sunline.saas.fee.arrangement.model.db.FeeArrangement
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementAdd
import cn.sunline.saas.fee.arrangement.repository.FeeArrangementRepository
import cn.sunline.saas.fee.util.FeeUtil
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: FeeArrangementService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/25 9:03
 */
@Service
class FeeArrangementService(private val feeArrangementRepos: FeeArrangementRepository) :
    BaseMultiTenantRepoService<FeeArrangement, Long>(feeArrangementRepos) {

    @Autowired
    private lateinit var seq: Sequence

    fun registered(
        agreementId: Long, dtoFeeArrangements: MutableList<DTOFeeArrangementAdd>
    ): MutableList<FeeArrangement> {
        val feeArrangements = mutableListOf<FeeArrangement>()
        dtoFeeArrangements.forEach {
            FeeUtil.validFeeConfig(it.feeMethodType, it.feeAmount, it.feeRate)
            feeArrangements.add(
                FeeArrangement(
                    id = seq.nextId(),
                    agreementId = agreementId,
                    feeType = it.feeType,
                    feeMethodType = it.feeMethodType,
                    feeAmount = it.feeAmount,
                    feeRate = it.feeRate,
                    feeDeductType = it.feeDeductType
                )
            )
        }

        return save(feeArrangements).toMutableList()
    }
}