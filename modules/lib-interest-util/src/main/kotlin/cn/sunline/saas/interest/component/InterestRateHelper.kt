package cn.sunline.saas.interest.component

import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.exception.InterestRateNullException
import cn.sunline.saas.interest.model.InterestRate
import java.math.BigDecimal

/**
 * @title: InterestRateHelper
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/19 10:31
 */
object InterestRateHelper {

    fun getTermOrAmountRate(
        loanAmount: BigDecimal,
        loanTerm: LoanTermType,
        rates: MutableList<InterestRate>?
    ): BigDecimal? {
        val rate = rates?.firstOrNull()
        return if (rate?.toPeriod != null) {
            rates.sortBy { item -> item.toPeriod }
            rates.firstOrNull { it.toPeriod!!.term >= loanTerm.term }?.rate
        } else if (rate?.toAmountPeriod != null) {
            rates.sortBy { item -> item.toAmountPeriod }
            rates.firstOrNull { it.toAmountPeriod!! >= loanAmount }?.rate
        } else {
            null
        }
    }

    fun getExecutionRate(
        interestType: InterestType,
        term: LoanTermType,
        amount: BigDecimal,
        basicPoint: BigDecimal?,
        floatRatio: BigDecimal?,
        baseInterestRate: MutableList<InterestRate>,
        customInterestRate: MutableList<InterestRate>,
    ): BigDecimal {
        val baseRate = getTermOrAmountRate(amount, term, baseInterestRate)
            ?: throw InterestRateNullException("base rate must be not null when interest type is floating rate")
        val rate = getTermOrAmountRate(amount, term, customInterestRate)
            ?: throw InterestRateNullException("custom rate must be not null when interest type is floating rate")
        return when (interestType) {
            InterestType.FIXED -> baseRate
            InterestType.FLOATING_RATE_NOTE -> {  // baseRate * (1+basePoint) + customRate
                CalculateInterestRate(baseRate).calRateWithNoPercent(
                    rate,
                    basicPoint ?: BigDecimal.ZERO,
                    floatRatio ?: BigDecimal.ZERO
                )
            }
        }
    }
}