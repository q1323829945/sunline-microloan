package cn.sunline.saas.interest.dto

import cn.sunline.saas.interest.model.RatePlanType


data class DTORatePlanAdd(
    val name: String,
    val type: RatePlanType
)
