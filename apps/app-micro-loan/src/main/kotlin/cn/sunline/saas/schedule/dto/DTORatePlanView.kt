package cn.sunline.saas.schedule.dto

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.RatePlanType
import java.math.BigDecimal

data class DTORatePlanView(
    val id: String,
    val name: String,
    val type: RatePlanType,
    val rates: List<DTORatesView>
)

data class DTORatesView(
    val id:Long,
    val period: LoanTermType,
    val rate: String
)

