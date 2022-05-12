package cn.sunline.saas.repayment.instruction.model.dto

import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal

/**
 * @title: DTORepaymentInstruction
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/22 16:49
 */
data class DTORepaymentInstruction(
    val moneyTransferInstructionAmount: BigDecimal,
    val moneyTransferInstructionCurrency: CurrencyType,
    val moneyTransferInstructionPurpose: String?,
    val payeeAccount: String,
    val payerAccount: String,
    val agreementId: Long,
    val businessUnit: Long
)
