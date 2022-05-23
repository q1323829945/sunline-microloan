package cn.sunline.saas.interest.arrangement.component

import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.interest.arrangement.exception.BaseRateNullException
import cn.sunline.saas.interest.arrangement.model.db.InterestArrangement
import cn.sunline.saas.interest.constant.InterestType
import java.math.BigDecimal

/**
 * @title: InterestArrangementComponent
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/23 15:30
 */
fun InterestArrangement.getExecutionRate(): BigDecimal {
    val executionRate = when (interestType) {
        InterestType.FIXED -> rate
        InterestType.FLOATING_RATE_NOTE -> if (baseRate == null) {
            throw BaseRateNullException("base rate must be not null when interest type is floating rate")
        } else {
            CalculateInterestRate(baseRate).calRate(rate)
        }
    }
    return executionRate
}