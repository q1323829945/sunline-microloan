package cn.sunline.saas.interest.dto

import cn.sunline.saas.global.constant.LoanTermType


data class DTOInterestRateView(
    val id: Long,
    val period: LoanTermType,
    val rate: String,
    val ratePlanId:Long
)