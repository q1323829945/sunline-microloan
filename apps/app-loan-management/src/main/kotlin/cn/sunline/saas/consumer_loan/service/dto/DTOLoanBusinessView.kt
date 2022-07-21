package cn.sunline.saas.consumer_loan.service.dto

import cn.sunline.saas.global.constant.*
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.money.transfer.instruction.model.InstructionLifecycleStatus

data class DTOLoanBusinessView(
    val agreementId: String,
    val applicationId: String,
    val disbursementAccountId: String,
    val disbursementAccount: String,
    val loanProductType: LoanProductType,
    val currency: CurrencyType,
    val totalLoanAmount: String,
    val disbursementAmount: String,
    val repaymentAmount: String,
    val isSyndicatedLoan: YesOrNo,
    val isRevolvingLoan: YesOrNo,
    val status: AgreementStatus
)

data class DTOApplicationLoanView(
    val agreementId: String,
    val applicationId: String,
    val disbursementAccountId: String,
    val disbursementAccount: String,
    val productType: LoanProductType,
    val currency: CurrencyType,
    val totalLoanAmount: String,
    val totalRepayment: String,
    val totalAccountBalance: String,
    val isSyndicatedLoan: YesOrNo,
    val isRevolvingLoan: YesOrNo,
    val status: AgreementStatus
)

data class DTOFeeItemView(
    val agreementId: String,
    val applicationId: String,
    val loanFeeType: LoanFeeType,
    val loanFeeTypeName: String,
    val currency: CurrencyType,
    val feeAmountOrRatio: String,
    val nonPaymentAmount: String
)

data class DTOLoanDisbursementView(
    val agreementId: String,
    val applicationId: String,
    val disbursementAccount: String,
    val disbursementAccountBank: String,
    val disbursementAmount: String,
    val currency: CurrencyType,
    val fromDateTime: String,
    val toDateTime :String,
    val signedDate :String
)

data class DTOLoanHistoryEventView(
    val agreementId: String,
    val eventName:String,
    val eventDate:String?,
    val totalLoanAmount: String,
    val disbursementAmount: String,
    val repaidAmount: String
)

data class DTORepaymentRecordView(
    val id: String,
    val agreementId: String,
    val repaymentAmount: String,
    val currencyType: CurrencyType,
    val status: InstructionLifecycleStatus,
    val payerAccount: String,
    val userId: String,
    val customerId: String,
    val referenceId: String,
    val startDateTime: String?,
    val endDateTime: String?,
    val executeDateTime: String?
)