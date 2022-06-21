package cn.sunline.saas.consumer_loan.service.dto

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