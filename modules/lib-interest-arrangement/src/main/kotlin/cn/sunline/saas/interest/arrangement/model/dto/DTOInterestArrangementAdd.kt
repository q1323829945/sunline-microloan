package cn.sunline.saas.interest.arrangement.model.dto

import cn.sunline.saas.interest.constant.BaseYearDays
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.InterestRate
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
    val overdueInterestRatePercentage: BigDecimal,
    val planRates: MutableList<InterestRate>
)