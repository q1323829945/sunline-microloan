package cn.sunline.saas.fee.arrangement.service

import cn.sunline.saas.fee.arrangement.model.db.FeeItem
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeItemAdd
import cn.sunline.saas.fee.arrangement.repository.FeeItemRepository
import cn.sunline.saas.global.constant.RepaymentStatus
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import kotlinx.coroutines.currentCoroutineContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Root

@Service
class FeeItemService(private val feeItemRepos: FeeItemRepository) :
    BaseMultiTenantRepoService<FeeItem, Long>(feeItemRepos) {

    @Autowired
    private lateinit var seq: Sequence

    fun registered(
        agreementId: Long, dtoFeeItems: MutableList<DTOFeeItemAdd>
    ): MutableList<FeeItem> {
        val feeItems = mutableListOf<FeeItem>()
        dtoFeeItems.forEach {
            feeItems.add(
                FeeItem(
                    id = seq.nextId(),
                    agreementId = agreementId,
                    feeArrangementId = it.feeArrangementId,
                    feeAmount = it.feeAmount,
                    repaymentAmount = BigDecimal.ZERO,
                    feeRepaymentDate = it.feeRepaymentDate,
                    feeFromDate = it.feeFromDate,
                    feeToDate = it.feeToDate,
                    feeUser = it.feeUser,
                    currency = it.currencyType,
                    repaymentStatus = RepaymentStatus.UNDO
                )
            )
        }
        return save(feeItems).toMutableList()
    }

    fun listByAgreementId(agreementId: Long): MutableList<FeeItem> {
        val agreementIdSpecification: Specification<FeeItem> =
            Specification { root: Root<FeeItem>, _, criteriaBuilder ->
                val path: Expression<Long> = root.get("agreementId")
                val predicate = criteriaBuilder.equal(path, agreementId)
                criteriaBuilder.and(predicate)
            }

        return getPageWithTenant(agreementIdSpecification, Pageable.unpaged()).toMutableList()
    }
}