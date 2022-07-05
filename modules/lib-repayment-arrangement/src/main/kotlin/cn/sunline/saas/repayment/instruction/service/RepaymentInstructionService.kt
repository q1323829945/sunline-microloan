package cn.sunline.saas.repayment.instruction.service

import cn.sunline.saas.money.transfer.instruction.model.InstructionLifecycleStatus
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstruction
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstructionType
import cn.sunline.saas.money.transfer.instruction.repository.MoneyTransferInstructionRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.repayment.instruction.exception.RepaymentInstructionNotFoundException
import cn.sunline.saas.repayment.instruction.model.dto.DTORepaymentInstruction
import cn.sunline.saas.repayment.instruction.model.dto.DTORepaymentInstructionAdd
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

/**
 * @title: RepaymentInstructionService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/22 14:36
 */
@Service
class RepaymentInstructionService(private val moneyTransferInstructionRepo: MoneyTransferInstructionRepository) :
    BaseMultiTenantRepoService<MoneyTransferInstruction, Long>(moneyTransferInstructionRepo) {

    @Autowired
    private lateinit var seq: Sequence

    fun registered(dtoRepaymentInstructionAdd: DTORepaymentInstructionAdd): MoneyTransferInstruction {
        val moneyTransferInstruction = MoneyTransferInstruction(
            id = seq.nextId(),
            moneyTransferInstructionType = MoneyTransferInstructionType.REPAYMENT,
            moneyTransferInstructionAmount = dtoRepaymentInstructionAdd.moneyTransferInstructionAmount,
            moneyTransferInstructionCurrency = dtoRepaymentInstructionAdd.moneyTransferInstructionCurrency,
            moneyTransferInstructionPurpose = dtoRepaymentInstructionAdd.moneyTransferInstructionPurpose,
            moneyTransferInstructionStatus = InstructionLifecycleStatus.PREPARED,
            payeeAccount = dtoRepaymentInstructionAdd.payeeAccount,
            payerAccount = dtoRepaymentInstructionAdd.payerAccount,
            agreementId = dtoRepaymentInstructionAdd.agreementId,
            businessUnit = dtoRepaymentInstructionAdd.businessUnit,
            referenceId = dtoRepaymentInstructionAdd.referenceId,
            startDateTime = dtoRepaymentInstructionAdd.startDate,
            endDateTime = null,
            executeDateTime = null,
            operator = dtoRepaymentInstructionAdd.operator
        )

        return moneyTransferInstructionRepo.save(moneyTransferInstruction)
    }

    fun retrieve(id: Long): DTORepaymentInstruction {
        val transferInstruction =
            getOne(id) ?: throw RepaymentInstructionNotFoundException("repayment instruction not found")

        return DTORepaymentInstruction(
            id = transferInstruction.id,
            instructionAmount = transferInstruction.moneyTransferInstructionAmount.toPlainString(),
            instructionCurrency = transferInstruction.moneyTransferInstructionCurrency,
            instructionPurpose = transferInstruction.moneyTransferInstructionPurpose,
            payeeAccount = transferInstruction.payeeAccount,
            payerAccount = transferInstruction.payerAccount,
            agreementId = transferInstruction.agreementId,
            businessUnit = transferInstruction.businessUnit,
            referenceId = transferInstruction.referenceId!!
        )
    }

    fun getPage(
        agreementId: Long?,
        customerId: Long?,
        moneyTransferInstructionType: MoneyTransferInstructionType,
        moneyTransferInstructionStatus: InstructionLifecycleStatus?,
        pageable: Pageable
    ): Page<MoneyTransferInstruction> {
        val pageSort = if (pageable == Pageable.unpaged()) pageable else PageRequest.of(
            pageable.pageNumber,
            pageable.pageSize,
            Sort.by(Sort.Order.desc("startDateTime"))
        )
        return getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            agreementId?.run {
                predicates.add(
                    criteriaBuilder.equal(
                        root.get<Long>("agreementId"),
                        agreementId
                    )
                )
            }
            customerId?.run {
                predicates.add(
                    criteriaBuilder.equal(
                        root.get<Long>("businessUnit"),
                        customerId
                    )
                )
            }
            moneyTransferInstructionType.run {
                predicates.add(
                    criteriaBuilder.equal(
                        root.get<MoneyTransferInstructionType>("moneyTransferInstructionType"),
                        moneyTransferInstructionType
                    )
                )
            }
            moneyTransferInstructionStatus?.run {
                predicates.add(
                    criteriaBuilder.equal(
                        root.get<MoneyTransferInstructionType>(
                            "moneyTransferInstructionStatus"
                        ), moneyTransferInstructionStatus
                    )
                )
            }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageSort)
    }

    fun getPageByInvoiceId(
        invoiceId: Long, pageable: Pageable
    ): Page<MoneyTransferInstruction> {
        return getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            invoiceId.run {
                predicates.add(
                    criteriaBuilder.equal(
                        root.get<Long>("referenceId"),
                        invoiceId
                    )
                )
            }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable)
    }
}