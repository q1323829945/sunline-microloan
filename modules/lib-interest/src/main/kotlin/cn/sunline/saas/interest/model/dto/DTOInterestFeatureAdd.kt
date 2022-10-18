package cn.sunline.saas.interest.model.dto

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.interest.constant.InterestType
import java.math.BigDecimal

/**
 * @title: DTOInterestFeatureAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/10 14:55
 */
data class DTOInterestFeatureAdd(
    val interestType: InterestType,
    val ratePlanId: Long,
    val interest: DTOInterestFeatureModalityAdd,
    val overdueInterest: DTOOverdueInterestFeatureModalityAdd
)

data class DTOInterestFeatureModalityAdd(
    val baseYearDays: BaseYearDays,
    val adjustFrequency: String?,
    val basicPoint: BigDecimal?,
    val floatRatio: BigDecimal?,
)

data class DTOOverdueInterestFeatureModalityAdd(
    var overdueInterestRatePercentage: Long
)
