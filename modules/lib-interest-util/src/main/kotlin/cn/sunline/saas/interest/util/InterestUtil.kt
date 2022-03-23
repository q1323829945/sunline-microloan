package cn.sunline.saas.interest.util

import org.joda.time.Days
import java.math.BigDecimal
import java.math.RoundingMode
import org.joda.time.Instant

/**
 * @title: InterestUtil
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/23 11:07
 */
object InterestUtil {
    private const val SCALE = 2

    fun calYearInterest(amount: BigDecimal, interestRateYear: BigDecimal): BigDecimal {
        return amount.multiply(interestRateYear).divide(BigDecimal(100), SCALE, RoundingMode.HALF_UP)
    }

    fun calMonthInterest(amount: BigDecimal, interestRateMonth: BigDecimal): BigDecimal {
        return amount.multiply(interestRateMonth).divide(BigDecimal(100), SCALE, RoundingMode.HALF_UP)
    }

    fun calDaysInterest(
        amount: BigDecimal,
        interestRateDays: BigDecimal,
        startDay: Instant,
        endDay: Instant
    ): BigDecimal {
        val days = Days.daysBetween(startDay, endDay).days
        return amount.multiply(interestRateDays).multiply(BigDecimal(days))
            .divide(BigDecimal(100), SCALE, RoundingMode.HALF_UP)
    }

}
