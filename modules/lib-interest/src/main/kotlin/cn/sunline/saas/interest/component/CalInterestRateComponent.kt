package cn.sunline.saas.interest.component

import cn.sunline.saas.interest.model.BaseYearDays
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * @title: CalculationInterestRateComponent
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 15:51
 */
object CalInterestRateComponent {

    private val mc = MathContext(6, RoundingMode.HALF_UP)

    fun toDayRate(baseYearDays: BaseYearDays, yearInterestRate: BigDecimal): BigDecimal {
        return yearInterestRate.divide(BigDecimal(baseYearDays.getDays()), mc)
    }

    fun toMonthRate(yearInterestRate: BigDecimal): BigDecimal {
        return yearInterestRate.divide(BigDecimal(12, mc))
    }

    fun calOverdueInterestRate(yearInterestRate: BigDecimal, overdueInterestRatePercentage: Int): BigDecimal {
        return yearInterestRate.multiply(BigDecimal(overdueInterestRatePercentage)).divide(BigDecimal(100), mc)
    }
}