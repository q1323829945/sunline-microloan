package cn.sunline.saas.repayment.instruction.model.dto

import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal
import java.util.*

/**
 * @title: DTORepaymentInstruction
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/22 16:49
 */
data class DTORepaymentInstructionAdd(
    val moneyTransferInstructionAmount: BigDecimal,
    val moneyTransferInstructionCurrency: CurrencyType,
    val moneyTransferInstructionPurpose: String?,
    val payeeAccount: String?,
    val payerAccount: String?,
    val agreementId: Long,
    val businessUnit: Long,
    val referenceId: Long,
    val startDate: Date,
    val operator: String?
)

data class DTORepaymentInstruction(
    val id: Long,
    val instructionAmount: String,
    val instructionCurrency: CurrencyType,
    val instructionPurpose: String?,
    val payeeAccount: String?,
    val payerAccount: String?,
    val agreementId: Long,
    val businessUnit: Long,
    val referenceId: Long
)
