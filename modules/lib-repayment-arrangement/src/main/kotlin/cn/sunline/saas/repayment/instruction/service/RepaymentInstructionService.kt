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
import org.springframework.stereotype.Service

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
            businessUnit = dtoRepaymentInstructionAdd.businessUnit
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
            businessUnit = transferInstruction.businessUnit
        )
    }

}