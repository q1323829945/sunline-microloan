package cn.sunline.saas.interest.arrangement.component

import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.arrangement.exception.BaseRateNullException
import cn.sunline.saas.interest.arrangement.model.db.InterestArrangement
import cn.sunline.saas.interest.component.InterestRateHelper
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.RatePlanType
import java.math.BigDecimal

/**
 * @title: InterestArrangementComponent
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/23 15:30
 */
fun InterestArrangement.getExecutionRate(): BigDecimal {
    val executionRate = InterestRateHelper.getExecutionRate(
        interestType,
        floatPoint ?: BigDecimal.ZERO,
        floatRatio ?: BigDecimal.ZERO,
        baseRate,
        rate
    )
    return executionRate ?: throw BaseRateNullException("execution rate is null")
}