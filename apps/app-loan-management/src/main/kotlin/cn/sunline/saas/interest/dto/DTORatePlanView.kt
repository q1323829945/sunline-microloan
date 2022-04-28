package cn.sunline.saas.interest.dto

import cn.sunline.saas.interest.model.RatePlanType


data class DTORatePlanView(
    val id: String,
    val name: String,
    val type: RatePlanType,
    val rates: List<DTORatesView>
)