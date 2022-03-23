package cn.sunline.saas.interest.util

import cn.sunline.saas.interest.constant.BaseYearDays
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * @title: CalculationInterestRateComponent
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 15:51
 */
object InterestRateUtil {

    private const val SCALE = 6

    fun toDayRate(baseYearDays: BaseYearDays, yearInterestRate: BigDecimal): BigDecimal {
        return yearInterestRate.divide(BigDecimal(baseYearDays.days),SCALE,RoundingMode.HALF_UP)
    }

    fun toMonthRate(yearInterestRate: BigDecimal): BigDecimal {
        return yearInterestRate.divide(BigDecimal(12),SCALE,RoundingMode.HALF_UP)
    }

    fun calOverdueInterestRate(yearInterestRate: BigDecimal, overdueInterestRatePercentage: BigDecimal): BigDecimal {
        return yearInterestRate.multiply(overdueInterestRatePercentage).divide(BigDecimal(100),SCALE,RoundingMode.HALF_UP)
    }
}