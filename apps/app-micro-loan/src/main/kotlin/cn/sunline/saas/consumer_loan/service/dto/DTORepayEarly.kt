package cn.sunline.saas.consumer_loan.service.dto

data class DTORepayEarly(
    val agreementId: Long,
    val currency: String,
    val principal: String,
    val interest: String,
    val fee: String,
)
