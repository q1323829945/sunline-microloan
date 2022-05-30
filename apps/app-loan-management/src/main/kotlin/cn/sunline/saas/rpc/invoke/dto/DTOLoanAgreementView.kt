package cn.sunline.saas.rpc.invoke.dto

import cn.sunline.saas.global.constant.AgreementStatus

data class DTOLoanAgreementView (
    val id:String,
    val status: AgreementStatus,
)