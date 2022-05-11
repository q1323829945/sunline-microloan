package cn.sunline.saas.interest.controller.dto

import cn.sunline.saas.global.constant.LoanTermType

data class DTOInterestRate(
    val id: String? = null,
    val period: LoanTermType,
    val rate: String,
    val ratePlanId:String
)
