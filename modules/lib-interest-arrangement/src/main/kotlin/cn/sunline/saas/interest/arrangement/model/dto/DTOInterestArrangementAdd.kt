package cn.sunline.saas.interest.arrangement.model.dto

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanAmountTierType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.constant.InterestType
import java.math.BigDecimal

/**
 * @title: DTOInterestArrangementAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/23 15:11
 */
data class DTOInterestArrangementAdd(
    val interestType: InterestType,
    val baseYearDays: BaseYearDays,
    val adjustFrequency: String,
    val overdueInterestRatePercentage: String,
    val planRates: MutableList<DTOInterestRate>,
    val baseRate: String?,
    val floatPoint: BigDecimal?,
    val floatRatio: BigDecimal?
)

data class DTOInterestRate(
    val id: Long,
    val fromPeriod: LoanTermType? = null,
    val toPeriod: LoanTermType? = null,
    val fromAmountPeriod: BigDecimal? = null,
    val toAmountPeriod: BigDecimal? = null,
    val rate: String
)