package cn.sunline.saas.consumer_loan.service.dto

data class DTORepaymentAccountAdd(
    val agreementId: String,
    val repaymentAccount: String,
    val repaymentAccountBank: String,
    val mobileNumber: String
)