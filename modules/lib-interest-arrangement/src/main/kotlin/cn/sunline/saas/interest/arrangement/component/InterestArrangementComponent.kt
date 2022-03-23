package cn.sunline.saas.interest.arrangement.component

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.arrangement.model.db.InterestArrangement
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.util.InterestRateUtil
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
        InterestType.FLOATING_RATE_NOTE -> InterestRateUtil.calRate(
            InterestRateUtil.getRate(term, baseRates),
            rate
        )
    }
    return executionRate
}