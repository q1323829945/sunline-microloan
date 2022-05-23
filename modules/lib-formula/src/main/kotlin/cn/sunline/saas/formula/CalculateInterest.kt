package cn.sunline.saas.formula

import cn.sunline.saas.formula.constant.CalculatePrecision
import cn.sunline.saas.global.constant.BaseYearDays
import org.joda.time.Days
import org.joda.time.Instant
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

    fun getDaysInterest(startDay: Instant, endDay: Instant,baseYearDays: BaseYearDays): BigDecimal {
        val days = Days.daysBetween(startDay, endDay).days
        return  getDayInterest(days,baseYearDays)
    }

    private fun calculateInterest(interestRate:BigDecimal,num:Int):BigDecimal{
        return principal.multiply(interestRate).multiply(BigDecimal(num)).setScale(CalculatePrecision.INTEREST, RoundingMode.HALF_UP)
    }

}
