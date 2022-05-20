package cn.sunline.saas.rpc.invoke.dto

import cn.sunline.saas.global.constant.*


data class DTOInterestRate(
    val id: String,
    val period: LoanTermType,
    val rate: String,
)