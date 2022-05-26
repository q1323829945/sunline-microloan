package cn.sunline.saas.fee.arrangement.service

import cn.sunline.saas.fee.arrangement.model.db.FeeArrangement
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementAdd
import cn.sunline.saas.fee.arrangement.repository.FeeArrangementRepository
import cn.sunline.saas.fee.util.FeeUtil
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Root

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

    fun listByAgreementId(agreementId: Long): MutableList<FeeArrangement> {
        val agreementIdSpecification: Specification<FeeArrangement> =
            Specification { root: Root<FeeArrangement>, _, criteriaBuilder ->
                val path: Expression<Long> = root.get("agreementId")
                val predicate = criteriaBuilder.equal(path, agreementId)
                criteriaBuilder.and(predicate)
            }

        return getPageWithTenant(agreementIdSpecification, Pageable.unpaged()).toMutableList()
    }
}