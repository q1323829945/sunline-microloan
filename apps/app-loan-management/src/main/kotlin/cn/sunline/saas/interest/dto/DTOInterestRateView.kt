package cn.sunline.saas.interest.dto

import cn.sunline.saas.global.constant.LoanTermType


data class DTOInterestRateView(
    val id: String,
    val period: LoanTermType,
    val rate: String,
    val ratePlanId:String
)