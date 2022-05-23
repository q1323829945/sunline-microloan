package cn.sunline.saas.interest.arrangement.component

import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.arrangement.model.db.InterestArrangement
import cn.sunline.saas.interest.component.InterestRateHelper
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.InterestRate
import java.math.BigDecimal

/**
 * @title: InterestArrangementComponent
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/23 15:30
 */
fun InterestArrangement.getExecutionRate(term: LoanTermType, baseRates: MutableList<InterestRate>): BigDecimal {
    val executionRate = when (interestType) {
        InterestType.FIXED -> rate
        InterestType.FLOATING_RATE_NOTE -> CalculateInterestRate(InterestRateHelper.getRate(term, baseRates)).calRate(
            rate
        )
    }
    return executionRate
}