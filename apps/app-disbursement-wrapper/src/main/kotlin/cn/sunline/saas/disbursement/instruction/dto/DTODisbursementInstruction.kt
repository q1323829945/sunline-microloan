package cn.sunline.saas.disbursement.instruction.dto

import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal

/**
 * @title: DTODisbursementInstruction
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/21 10:45
 */
data class DTODisbursementInstruction(
    val moneyTransferInstructionAmount: BigDecimal,
    val moneyTransferInstructionCurrency: CurrencyType,
    val moneyTransferInstructionPurpose: String?,
    val payeeAccount: String,
    val payerAccount: String,
    val agreementId: Long
)
