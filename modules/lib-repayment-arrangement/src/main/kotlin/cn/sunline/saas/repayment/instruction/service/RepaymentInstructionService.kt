package cn.sunline.saas.repayment.instruction.service

import cn.sunline.saas.money.transfer.instruction.model.InstructionLifecycleStatus
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstruction
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstructionType
import cn.sunline.saas.money.transfer.instruction.repository.MoneyTransferInstructionRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.repayment.instruction.model.dto.DTORepaymentInstruction
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
class RepaymentInstructionService (private val moneyTransferInstructionRepo: MoneyTransferInstructionRepository) :
BaseMultiTenantRepoService<MoneyTransferInstruction, Long>(moneyTransferInstructionRepo) {

    @Autowired
    private lateinit var seq: Sequence

    fun registered(dtoRepaymentInstruction: DTORepaymentInstruction): MoneyTransferInstruction {
        val moneyTransferInstruction = MoneyTransferInstruction(
            id = seq.nextId(),
            moneyTransferInstructionType = MoneyTransferInstructionType.REPAYMENT,
            moneyTransferInstructionAmount = dtoRepaymentInstruction.moneyTransferInstructionAmount,
            moneyTransferInstructionCurrency = dtoRepaymentInstruction.moneyTransferInstructionCurrency,
            moneyTransferInstructionPurpose = dtoRepaymentInstruction.moneyTransferInstructionPurpose,
            moneyTransferInstructionStatus = InstructionLifecycleStatus.PREPARED,
            payeeAccount = dtoRepaymentInstruction.payeeAccount,
            payerAccount = dtoRepaymentInstruction.payerAccount,
            agreementId = dtoRepaymentInstruction.agreementId
        )

        return moneyTransferInstructionRepo.save(moneyTransferInstruction)
    }
}