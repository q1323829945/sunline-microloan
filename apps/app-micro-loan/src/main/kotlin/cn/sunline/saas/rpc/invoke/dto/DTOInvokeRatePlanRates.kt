package cn.sunline.saas.rpc.invoke.dto

import cn.sunline.saas.global.constant.LoanAmountTierType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.RatePlanType
import java.math.BigDecimal

data class DTOInvokeRatePlanRates(
    val id: String,
    val name: String,
    val type: RatePlanType,
    val rates: List<DTOInvokeRates>
)

data class DTOInvokeRates(
    val id:String,
    val fromPeriod: LoanTermType? = null,
    val toPeriod: LoanTermType? = null,
    val fromAmountPeriod: BigDecimal? = null,
    val toAmountPeriod: BigDecimal? = null,
    val rate: String
)