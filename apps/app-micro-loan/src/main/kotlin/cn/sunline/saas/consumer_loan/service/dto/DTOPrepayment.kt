package cn.sunline.saas.consumer_loan.service.dto

import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.money.transfer.instruction.model.InstructionLifecycleStatus
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstructionType
import java.math.BigDecimal

data class DTOPrepayment(
    val agreementId: Long,
    val currency: String,
    val principal: String,
    val interest: String,
    val fee: String,
    val repaymentAccountId: Long,
    val repaymentAccount: String
)


data class DTOBankListView (
    val id: String,
    val name: String,
    val code: String,
)

data class DTOVerifyCode(
    val mobilePhone: String,
    val code: String
)


data class DTORepaymentRecordView(
    val id: String,
    val repaymentAmount: BigDecimal,
    val currencyType: CurrencyType,
    val status: InstructionLifecycleStatus,
    val payerAccount: String,
    val agreementId: String,
    val userId: String,
    val customerId: String,
    val referenceId: String,
    val startDateTime: String?,
    val endDateTime: String?,
    val executeDateTime: String?
)