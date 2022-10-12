package cn.sunline.saas.formula

import cn.sunline.saas.global.constant.CalculatePrecision
import cn.sunline.saas.global.constant.BaseYearDays
import org.joda.time.DateTime
import org.joda.time.Days
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @title: InterestUtil
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/23 11:07
 */
class CalculateInterest(private val principal: BigDecimal, private val interestRateYear: CalculateInterestRate) {

    fun getYearInterest(years:Int = 1): BigDecimal {
        return calculateInterest(interestRateYear.getYearRate(),years)
    }

    fun getMonthInterest(months: Int = 1): BigDecimal {
        return calculateInterest(interestRateYear.toMonthRate(),months)
    }

    fun getDayInterest(days:Int = 1,baseYearDays: BaseYearDays):BigDecimal{
        return calculateInterest(interestRateYear.toDayRate(baseYearDays),days)
    }

    fun getDaysInterest(startDay: DateTime, endDay: DateTime,baseYearDays: BaseYearDays): BigDecimal {
        val days = Days.daysBetween(startDay, endDay).days
        return  getDayInterest(days,baseYearDays)
    }

    private fun calculateInterest(interestRate:BigDecimal,num:Int):BigDecimal{
        return principal.multiply(interestRate).multiply(BigDecimal(num)).setScale(CalculatePrecision.INTEREST, RoundingMode.HALF_UP)
    }

    fun getFirstInterest(
        periods: Int,
        periodDates: MutableList<CalculatePeriod.PeriodDate>,
        baseYearDays: BaseYearDays
    ): BigDecimal {
        var firstInterest = BigDecimal.ZERO
        if (periods != periodDates.size) {
            val periodDate = periodDates.first()
            firstInterest = CalculateInterest(principal, interestRateYear).getDaysInterest(
                periodDate.fromDateTime,
                periodDate.toDateTime,
                baseYearDays
            ).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
        }
        return firstInterest
    }
}
