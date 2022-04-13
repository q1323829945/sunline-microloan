package cn.sunline.saas.interest.dto

import cn.sunline.saas.interest.model.RatePlanType

data class DTOSimpleRatePlanView(
    val id: String,
    val name: String,
    val type: RatePlanType
)