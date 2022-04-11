package cn.sunline.saas.interest.dto

import cn.sunline.saas.interest.model.RatePlanType


data class DTORatePlanChange(
    val id: Long,
    val name: String,
    val type: RatePlanType
)