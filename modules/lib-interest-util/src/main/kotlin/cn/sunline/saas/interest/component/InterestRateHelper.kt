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
        floatPoint: BigDecimal?,
        floatRatio: BigDecimal?,
        rates: MutableList<InterestRate>,
    ): BigDecimal {

        return when (interestType) {
            InterestType.FIXED -> { // floatRate
                val baseRate = getTermOrAmountRate(amount, term, rates)
                    ?: throw InterestRateNullException("custom rate must be not null when interest type is fixed rate")
                getExecutionRate(interestType,floatPoint,floatRatio,baseRate,null)?: throw InterestRateNullException("execution rate is null")
            }

            InterestType.FLOATING_RATE_NOTE -> {  // basicRate * ( 1 + floatRatio) + floatPoint
                val floatRate = getTermOrAmountRate(amount, term, rates)
                    ?: throw InterestRateNullException("base rate must be not null when interest type is floating rate")
                getExecutionRate(interestType,floatPoint,floatRatio,null,floatRate)?: throw InterestRateNullException("execution rate is null")
            }
        }
    }

    fun getExecutionRate(
        interestType: InterestType,
        floatPoint: BigDecimal?,
        floatRatio: BigDecimal?,
        baseRate: BigDecimal?,
        floatRate: BigDecimal?,
    ): BigDecimal {
        return when (interestType) {
            InterestType.FIXED -> { // floatRate
                floatRate!!
            }
            InterestType.FLOATING_RATE_NOTE -> {  // basicRate * ( 1 + floatRatio) + floatPoint
                CalculateInterestRate(baseRate!!).calRateWithNoPercent(
                    floatPoint ?: BigDecimal.ZERO,
                    floatRatio ?: BigDecimal.ZERO
                )
            }
        }
    }
}