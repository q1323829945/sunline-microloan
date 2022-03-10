package cn.sunline.saas.interest.model.dto

import cn.sunline.saas.interest.model.BaseYearDays
import cn.sunline.saas.interest.model.InterestType

/**
 * @title: DTOInterestFeatureAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/10 14:55
 */
data class DTOInterestFeatureAdd(
    val interestType: InterestType,
    val ratePlanId: Long,
    val baseYearDays: BaseYearDays,
    val adjustFrequency: String,
    val overdueInterestRatePercentage: Long
)
