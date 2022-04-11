package cn.sunline.saas.interest.dto

import cn.sunline.saas.global.constant.LoanTermType

data class DTOInterestRateAdd(
    val period: LoanTermType,
    val rate: String,
    val ratePlanId:Long
)