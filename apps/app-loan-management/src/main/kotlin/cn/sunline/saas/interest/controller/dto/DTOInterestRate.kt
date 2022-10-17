package cn.sunline.saas.interest.controller.dto

import cn.sunline.saas.global.constant.LoanAmountTierType
import cn.sunline.saas.global.constant.LoanTermType
import java.math.BigDecimal

data class DTOInterestRate(
    val id: String? = null,
    val toPeriod: LoanTermType? = null,
    val toAmountPeriod: String? = null,
    val rate: String,
    val ratePlanId: String
)


data class DTOInterestRateView(
    val id: String? = null,
    val fromPeriod: LoanTermType? = null,
    val toPeriod: LoanTermType? = null,
    val fromAmountPeriod: String? = null,
    val toAmountPeriod: String? = null,
    val rate: String,
    val ratePlanId: String
)