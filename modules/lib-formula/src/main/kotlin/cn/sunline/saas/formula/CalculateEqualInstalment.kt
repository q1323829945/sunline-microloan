package cn.sunline.saas.formula

import cn.sunline.saas.formula.constant.CalculatePrecision
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @title: CalculateEqualInstalment
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/20 14:46
 */
object CalculateEqualInstalment {

    /**
     * formula = 〔principal×monthly_rate×(1+monthly_rate)^months〕÷〔(1+monthly_rate)^months-1〕
     */
    fun getInstalment(amount: BigDecimal,
                      interestRateYear: BigDecimal,periods: Int): BigDecimal {
        val monthlyRate = CalculateInterestRate(interestRateYear).toMonthRate()
        val i = BigDecimal.ONE.add(monthlyRate).pow(periods)

        return amount.multiply(monthlyRate).multiply(i).divide(
            i.subtract(BigDecimal.ONE), CalculatePrecision.AMOUNT, RoundingMode.HALF_UP
        )
    }
}