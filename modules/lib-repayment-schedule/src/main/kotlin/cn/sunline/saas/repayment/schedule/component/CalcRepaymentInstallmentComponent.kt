

package cn.sunline.saas.repayment.schedule.component

import cn.sunline.saas.repayment.model.RepaymentFrequency
import java.math.BigDecimal
import java.math.RoundingMode

object CalcRepaymentInstallmentComponent {

    fun calcBaseRepaymentInstallment (principal: BigDecimal, interest: BigDecimal): BigDecimal {
        //return yearInterestRate.divide(BigDecimal(baseYearDays.getDays()), CalInterestRateComponent.mc)
        return principal.add(interest).setScale(2, RoundingMode.HALF_UP)
    }


    fun calcCapitalInstallment (loanAmount: BigDecimal, loanRate: BigDecimal,periods: Int,repaymentFrequency: RepaymentFrequency): BigDecimal {
        // 每月还款金额 = [总贷款金额 * 月利率 * （1 + 月利率） ^ 还款月数] / [(1 + 利率) ^ 还款月数 -1]
        // 每期应还款额＝【借款本金×季度利率×（1＋季度利率）^还款期数】/【（1＋季度利率）^还款期数－1】
        val realLoanRate = loanRate.multiply(repaymentFrequency.getMonths().toBigDecimal())
        val factor = realLoanRate.add(BigDecimal.ONE).pow(periods)
        // 平均还款金额
        return loanAmount.multiply(realLoanRate).multiply(factor).divide(factor.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP)
    }

}