package cn.sunline.saas.rpc.pubsub.dto

import cn.sunline.saas.global.constant.AgreementStatus

data class DTOLoanAgreement(
    val id:String,
    val status:AgreementStatus
)

