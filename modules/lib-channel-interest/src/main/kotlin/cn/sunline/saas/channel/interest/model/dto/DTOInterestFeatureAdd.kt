package cn.sunline.saas.channel.interest.model.dto


import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.channel.interest.constant.InterestType

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