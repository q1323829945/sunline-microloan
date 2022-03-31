package cn.sunline.saas.repayment.schedule.component

import cn.sunline.saas.repayment.schedule.model.dto.DTOInterestCalculator
import org.joda.time.DateTime
import org.joda.time.Days
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

object CalcInterestComponent {

    fun calcBaseInterest(dtoInterestCalculator: DTOInterestCalculator): BigDecimal {
        val loanRateDay = dtoInterestCalculator.loanRateDay
        val currentRepaymentDateTime = dtoInterestCalculator.currentRepaymentDateTime
        val loanRateMonth = dtoInterestCalculator.loanRateMonth
        val nextRepaymentDateTime = dtoInterestCalculator.nextRepaymentDateTime
        val calcAmount = dtoInterestCalculator.calcAmount
        val repaymentFrequency = dtoInterestCalculator.repaymentFrequency
        val frequency = repaymentFrequency?.getMonths() ?: 1
        return if (nextRepaymentDateTime.compareTo(currentRepaymentDateTime.plusMonths(1 * frequency)) == 0) {
            loanRateMonth.multiply(calcAmount).multiply(frequency.toBigDecimal()).setScale(2,RoundingMode.HALF_UP)
        } else {
            val days = abs(Days.daysBetween(currentRepaymentDateTime, nextRepaymentDateTime).days)
            loanRateDay.multiply(calcAmount).multiply(days.toBigDecimal()).setScale(2,RoundingMode.HALF_UP)
        }
    }

    fun calcDayInterest(calcAmount: BigDecimal,loanRateDay: BigDecimal,currentRepaymentDateTime: DateTime,nextRepaymentDateTime: DateTime): BigDecimal {
        val days = abs(Days.daysBetween(currentRepaymentDateTime, nextRepaymentDateTime).days)
        return loanRateDay.multiply(calcAmount).multiply(days.toBigDecimal()).setScale(2,RoundingMode.HALF_UP)
    }
}