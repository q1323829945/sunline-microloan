package cn.sunline.saas.repayment.schedule.component

import cn.sunline.saas.repayment.model.PaymentMethodType
import cn.sunline.saas.repayment.schedule.model.dto.DTOPrincipalCalculator
import java.math.BigDecimal
import java.math.RoundingMode


object CalcPrincipalComponent {

    //private val mc = MathContext(6, RoundingMode.HALF_UP)

    fun calcBasePrincipal(dtoPrincipalCalculator: DTOPrincipalCalculator): BigDecimal {
        val calcAmount = dtoPrincipalCalculator.calcAmount
        val periods = dtoPrincipalCalculator.periods
        val period = dtoPrincipalCalculator.period
        val repaymentType = dtoPrincipalCalculator.paymentMethod
        val repaymentFrequency = dtoPrincipalCalculator.repaymentFrequency
        val loanRate = dtoPrincipalCalculator.interestRate

        //return yearInterestRate.divide(BigDecimal(baseYearDays.getDays()), CalInterestRateComponent.mc)
        return when (repaymentType) {
            PaymentMethodType.EQUAL_PRINCIPAL -> {
                calcAmount.divide(periods.toBigDecimal(),2, RoundingMode.HALF_UP)
            }
            PaymentMethodType.EQUAL_INSTALLMENT -> {
                //B=a*i(1+i)^(n-1)/[(1+i)^N-1]
                val realLoanRate = loanRate.multiply(repaymentFrequency?.getMonths()?.toBigDecimal())
                val factor = realLoanRate.add(BigDecimal.ONE).pow(period-1)
                val factor1 = realLoanRate.add(BigDecimal.ONE).pow(periods)?.subtract(BigDecimal.ONE)
                calcAmount.multiply(realLoanRate).multiply(factor).divide(factor1,2,RoundingMode.HALF_UP)
            }
            PaymentMethodType.PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY -> BigDecimal.ZERO
            PaymentMethodType.ONE_OFF_REPAYMENT -> BigDecimal.ZERO
        }
    }
}