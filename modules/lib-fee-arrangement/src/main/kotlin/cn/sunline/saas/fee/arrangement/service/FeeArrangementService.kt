package cn.sunline.saas.fee.arrangement.service

import cn.sunline.saas.fee.arrangement.model.db.FeeArrangement
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementAdd
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementView
import cn.sunline.saas.fee.arrangement.repository.FeeArrangementRepository
import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.fee.util.FeeUtil
import cn.sunline.saas.global.constant.LoanFeeType
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.math.BigDecimal
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

    data class FeeItem(val immediateFee: BigDecimal, val scheduleFee: BigDecimal)

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


    fun getDisbursementFeeItem(feeArrangement: MutableList<DTOFeeArrangementView>?, amount: BigDecimal): FeeItem {
        var feeImmediateAmount = BigDecimal.ZERO
        var feeScheduleAmount = BigDecimal.ZERO
        feeArrangement?.filter {
            it.feeType == LoanFeeType.DISBURSEMENT && it.feeDeductType == FeeDeductType.IMMEDIATE
        }?.forEach {
            feeImmediateAmount = when (it.feeMethodType) {
                FeeMethodType.FEE_RATIO -> {
                    feeImmediateAmount.add(FeeUtil.calFeeAmount(amount, it.feeRate!!, it.feeMethodType))
                }
                FeeMethodType.FIX_AMOUNT -> {
                    feeImmediateAmount.add(it.feeAmount)
                }
                else -> {
                    feeImmediateAmount
                }
            }
        }

        feeArrangement?.filter {
            it.feeType == LoanFeeType.DISBURSEMENT && it.feeDeductType == FeeDeductType.SCHEDULE
        }?.forEach {
            feeScheduleAmount = when (it.feeMethodType) {
                FeeMethodType.FEE_RATIO -> {
                    feeScheduleAmount.add(FeeUtil.calFeeAmount(amount, it.feeRate!!, it.feeMethodType))
                }
                FeeMethodType.FIX_AMOUNT -> {
                    feeScheduleAmount.add(it.feeAmount)
                }
                else -> {
                    feeScheduleAmount
                }
            }
        }

        return FeeItem(feeImmediateAmount, feeScheduleAmount)
    }

    fun getPrepaymentFeeItem(feeArrangement: MutableList<DTOFeeArrangementView>?, amount: BigDecimal): FeeItem {
        var feeImmediateAmount = BigDecimal.ZERO
        val feeScheduleAmount = BigDecimal.ZERO
        feeArrangement?.filter {
            it.feeType == LoanFeeType.PREPAYMENT
        }?.forEach {
            feeImmediateAmount = when (it.feeMethodType) {
                FeeMethodType.FEE_RATIO -> {
                    feeImmediateAmount.add(FeeUtil.calFeeAmount(amount, it.feeRate!!, it.feeMethodType))
                }
                FeeMethodType.FIX_AMOUNT -> {
                    feeImmediateAmount.add(it.feeAmount)
                }
                else -> {
                    feeImmediateAmount
                }
            }
        }

        return FeeItem(feeImmediateAmount, feeScheduleAmount)
    }

    fun getOverdueFeeItem(feeArrangement: MutableList<DTOFeeArrangementView>?, amount: BigDecimal): FeeItem {
        var feeImmediateAmount = BigDecimal.ZERO
        var feeScheduleAmount = BigDecimal.ZERO
        feeArrangement?.filter {
            it.feeType == LoanFeeType.OVERDUE && it.feeDeductType == FeeDeductType.IMMEDIATE
        }?.forEach {
            feeImmediateAmount = when (it.feeMethodType) {
                FeeMethodType.FEE_RATIO -> {
                    feeImmediateAmount.add(FeeUtil.calFeeAmount(amount, it.feeRate!!, it.feeMethodType))
                }
                FeeMethodType.FIX_AMOUNT -> {
                    feeImmediateAmount.add(it.feeAmount)
                }
                else -> {
                    feeImmediateAmount
                }
            }
        }

        feeArrangement?.filter {
            it.feeType == LoanFeeType.OVERDUE && it.feeDeductType == FeeDeductType.SCHEDULE
        }?.forEach {
            feeScheduleAmount = when (it.feeMethodType) {
                FeeMethodType.FEE_RATIO -> {
                    feeScheduleAmount.add(FeeUtil.calFeeAmount(amount, it.feeRate!!, it.feeMethodType))
                }
                FeeMethodType.FIX_AMOUNT -> {
                    feeScheduleAmount.add(it.feeAmount)
                }
                else -> {
                    feeScheduleAmount
                }
            }
        }

        return FeeItem(feeImmediateAmount, feeScheduleAmount)
    }
}