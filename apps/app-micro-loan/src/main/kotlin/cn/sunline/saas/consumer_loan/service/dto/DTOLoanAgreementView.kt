package cn.sunline.saas.consumer_loan.service.dto

import cn.sunline.saas.global.constant.AgreementStatus

data class DTOLoanAgreementView (
    val id:String,
    val status: AgreementStatus
)