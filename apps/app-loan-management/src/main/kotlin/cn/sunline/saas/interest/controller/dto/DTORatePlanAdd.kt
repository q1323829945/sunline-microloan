package cn.sunline.saas.interest.controller.dto

import cn.sunline.saas.interest.model.RatePlanType


data class DTORatePlan(
    val id: String?,
    val name: String,
    val type: RatePlanType
)

data class DTORatePlanWithInterestRates(
    val id: String,
    val name: String,
    val type: RatePlanType,
    val rates: List<DTORatesView>
)

data class DTORatesView(
    val id:String,
    val period: String,
    val rate: String
)