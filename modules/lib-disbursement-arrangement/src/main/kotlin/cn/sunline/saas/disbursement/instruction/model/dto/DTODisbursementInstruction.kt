package cn.sunline.saas.disbursement.instruction.model.dto

import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal

/**
 * @title: DTODisbursementInstruction
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/21 10:45
 */
data class DTODisbursementInstructionAdd(
    val moneyTransferInstructionAmount: BigDecimal,
    val moneyTransferInstructionCurrency: CurrencyType,
    val moneyTransferInstructionPurpose: String?,
    val payeeAccount: String?,
    val payerAccount: String?,
    val agreementId: Long,
    val businessUnit: Long
)

data class DTODisbursementInstruction(
    val id: Long,
    val instructionAmount: String,
    val instructionCurrency: CurrencyType,
    val instructionPurpose: String?,
    val payeeAccount: String?,
    val payerAccount: String?,
    val agreementId: Long,
    val businessUnit: Long,
)
