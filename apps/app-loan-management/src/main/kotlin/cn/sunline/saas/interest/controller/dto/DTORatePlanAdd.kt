package cn.sunline.saas.interest.controller.dto

import cn.sunline.saas.global.constant.LoanAmountTierType
import cn.sunline.saas.global.constant.LoanTermType
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
    val id: String,
    val fromPeriod: LoanTermType? = null,
    val toPeriod: LoanTermType? = null,
    val fromAmountPeriod: LoanAmountTierType? = null,
    val toAmountPeriod: LoanAmountTierType? = null,
    val rate: String,
)