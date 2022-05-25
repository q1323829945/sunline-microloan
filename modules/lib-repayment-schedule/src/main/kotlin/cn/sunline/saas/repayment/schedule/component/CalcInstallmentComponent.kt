package cn.sunline.saas.repayment.schedule.component

import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.repayment.schedule.model.dto.DTOPrincipalCalculator
import java.math.BigDecimal
import java.math.RoundingMode

object CalcInstallmentComponent {

    fun calcBaseRepaymentInstallment (principal: BigDecimal, interest: BigDecimal): BigDecimal {
        return principal.add(interest).setScale(2, RoundingMode.HALF_UP)
    }

    fun calcCapitalInstallment (loanAmount: BigDecimal, loanRate: BigDecimal,periods: Int,repaymentFrequency: RepaymentFrequency): BigDecimal {
        // 每月还款金额 = [总贷款金额 * 月利率 * （1 + 月利率） ^ 还款月数] / [(1 + 利率) ^ 还款月数 -1]
        // 每季应还款额＝【借款本金×季度利率×（1＋季度利率）^还款期数】/【（1＋季度利率）^还款期数－1】
        val realInterestMonthRate = loanRate.multiply(repaymentFrequency.term.num.toBigDecimal())
        val factor = realInterestMonthRate.add(BigDecimal.ONE).pow(periods)
        // 平均还款金额
        return loanAmount.multiply(realInterestMonthRate).multiply(factor).divide(factor.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP)
    }

    fun calcBasePrincipal(dtoPrincipalCalculator: DTOPrincipalCalculator): BigDecimal {
        val calcAmount = dtoPrincipalCalculator.calcAmount
        val periods = dtoPrincipalCalculator.periods
        val period = dtoPrincipalCalculator.period
        val paymentMethod = dtoPrincipalCalculator.paymentMethod
        val repaymentFrequency = dtoPrincipalCalculator.repaymentFrequency
        val interestMonthRate = dtoPrincipalCalculator.interestRate
        return when (paymentMethod) {
            PaymentMethodType.EQUAL_PRINCIPAL -> {
                calcAmount.divide(periods.toBigDecimal(),2, RoundingMode.HALF_UP)
            }
            PaymentMethodType.EQUAL_INSTALLMENT -> {
                //B=a*i(1+i)^(n-1)/[(1+i)^N-1]
                val realLoanRate = interestMonthRate.multiply(repaymentFrequency?.term?.num?.toBigDecimal())
                val factor = realLoanRate.add(BigDecimal.ONE).pow(period-1)
                val factor1 = realLoanRate.add(BigDecimal.ONE).pow(periods)?.subtract(BigDecimal.ONE)
                calcAmount.multiply(realLoanRate).multiply(factor).divide(factor1,2,RoundingMode.HALF_UP)
            }
            PaymentMethodType.PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY -> BigDecimal.ZERO
            PaymentMethodType.ONE_OFF_REPAYMENT -> BigDecimal.ZERO
        }
    }

}