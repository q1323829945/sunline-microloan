package cn.sunline.saas.interest.controller.dto

import cn.sunline.saas.global.constant.LoanAmountTierType
import cn.sunline.saas.global.constant.LoanTermTierType
import cn.sunline.saas.global.constant.LoanTermType

data class DTOInterestRate(
    val id: String? = null,
    val fromPeriod: LoanTermType? = null,
    val toPeriod: LoanTermType? = null,
    val fromAmountPeriod: LoanAmountTierType? = null,
    val toAmountPeriod: LoanAmountTierType? = null,
    val rate: String,
    val ratePlanId: String
)
