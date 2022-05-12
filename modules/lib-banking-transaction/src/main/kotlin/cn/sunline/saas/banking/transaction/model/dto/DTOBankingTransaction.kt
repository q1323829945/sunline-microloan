package cn.sunline.saas.banking.transaction.model.dto

import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal

/**
 * @title: DTOBankingTransaction
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/7 15:46
 */
data class DTOBankingTransaction(
    val name: String,
    val agreementId: Long,
    val instructionId: Long,
    val transactionDescription: String?,
    val currency: CurrencyType,
    val amount: BigDecimal,
    val businessUnit: Long,
    val appliedFee: BigDecimal?,
    val appliedRate: BigDecimal?,
    val customerId: Long
)
