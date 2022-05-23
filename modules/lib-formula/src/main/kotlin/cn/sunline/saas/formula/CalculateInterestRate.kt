package cn.sunline.saas.formula

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.formula.constant.CalculatePrecision
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @title: CalculationInterestRateComponent
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 15:51
 */
class CalculateInterestRate(private val yearInterestRatePercent: BigDecimal) {
    private val yearInterestRate: BigDecimal = yearInterestRatePercent.divide(BigDecimal(100))

    private fun dealPercent(num: BigDecimal): BigDecimal {
        return num.divide(BigDecimal(100), CalculatePrecision.INTEREST_RATE, RoundingMode.HALF_UP)
    }

    fun getYearRate():BigDecimal{
        return dealPercent(getYearRateWithPercent())
    }

    fun getYearRateWithPercent():BigDecimal{
        return yearInterestRatePercent
    }

    fun toDayRate(baseYearDays: BaseYearDays): BigDecimal {
        return dealPercent(toDayRateWithPercent(baseYearDays))
    }

    fun toDayRateWithPercent(baseYearDays: BaseYearDays): BigDecimal {
        return yearInterestRatePercent.divide(
            BigDecimal(baseYearDays.days),
            CalculatePrecision.INTEREST_RATE,
            RoundingMode.HALF_UP
        )
    }

    fun toMonthRate(): BigDecimal {
        return dealPercent(toMonthRateWithPercent())
    }

    fun toMonthRateWithPercent(): BigDecimal {
        return yearInterestRatePercent.divide(BigDecimal(12), CalculatePrecision.INTEREST_RATE, RoundingMode.HALF_UP)
    }

    fun calOverdueInterestRate(overdueInterestRatePercentage: BigDecimal): BigDecimal {
        return dealPercent(calOverdueInterestRateWithPercent(overdueInterestRatePercentage))
    }

    fun calOverdueInterestRateWithPercent(overdueInterestRatePercentage: BigDecimal): BigDecimal {
        return yearInterestRatePercent.multiply(overdueInterestRatePercentage)
            .divide(BigDecimal(100), CalculatePrecision.INTEREST_RATE, RoundingMode.HALF_UP)
    }

    fun calRateWithPercent(floatInterestRate: BigDecimal): BigDecimal {
        return yearInterestRatePercent.multiply(floatInterestRate)
            .divide(BigDecimal(100), CalculatePrecision.INTEREST_RATE, RoundingMode.HALF_UP)
            .add(yearInterestRatePercent)
    }

    fun calRate(floatInterestRate: BigDecimal): BigDecimal {
        return dealPercent(calRateWithPercent(floatInterestRate))
    }

}