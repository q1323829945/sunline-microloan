package cn.sunline.saas.disbursement.instruction.service

import cn.sunline.saas.disbursement.instruction.exception.DisbursementInstructionNotFoundException
import cn.sunline.saas.disbursement.instruction.model.dto.DTODisbursementInstruction
import cn.sunline.saas.disbursement.instruction.model.dto.DTODisbursementInstructionAdd
import cn.sunline.saas.money.transfer.instruction.model.InstructionLifecycleStatus
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstruction
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstructionType
import cn.sunline.saas.money.transfer.instruction.repository.MoneyTransferInstructionRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: DisbursementInstructionService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/21 10:37
 */
@Service
class DisbursementInstructionService(private val moneyTransferInstructionRepo: MoneyTransferInstructionRepository) :
    BaseMultiTenantRepoService<MoneyTransferInstruction, Long>(moneyTransferInstructionRepo) {

    @Autowired
    private lateinit var seq: Sequence

    fun registered(dtoDisbursementInstruction: DTODisbursementInstructionAdd): DTODisbursementInstruction {
        val moneyTransferInstruction = MoneyTransferInstruction(
            id = seq.nextId(),
            moneyTransferInstructionType = MoneyTransferInstructionType.DISBURSEMENT,
            moneyTransferInstructionAmount = dtoDisbursementInstruction.moneyTransferInstructionAmount,
            moneyTransferInstructionCurrency = dtoDisbursementInstruction.moneyTransferInstructionCurrency,
            moneyTransferInstructionPurpose = dtoDisbursementInstruction.moneyTransferInstructionPurpose,
            moneyTransferInstructionStatus = InstructionLifecycleStatus.PREPARED,
            payeeAccount = dtoDisbursementInstruction.payeeAccount,
            payerAccount = dtoDisbursementInstruction.payerAccount,
            agreementId = dtoDisbursementInstruction.agreementId,
            businessUnit = dtoDisbursementInstruction.businessUnit,
            referenceId = null,
            startDateTime = null,
            endDateTime = null,
            executeDateTime = null,
            operator = null
        )

        val transferInstruction = moneyTransferInstructionRepo.save(moneyTransferInstruction)
        return DTODisbursementInstruction(
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

    fun retrieve(id: Long): DTODisbursementInstruction {
        val transferInstruction =
            getOne(id) ?: throw DisbursementInstructionNotFoundException("disbursement instruction not found")

        return DTODisbursementInstruction(
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