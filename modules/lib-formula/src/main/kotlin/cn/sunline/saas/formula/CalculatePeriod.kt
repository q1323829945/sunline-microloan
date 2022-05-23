package cn.sunline.saas.formula

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import org.joda.time.Instant
import org.joda.time.format.DateTimeFormatter

/**
 * @title: CalculatePeriod
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/19 11:20
 */
object CalculatePeriod {

    /**
     * for now,the term is equaled to an integer multiple of frequency
     * for future,the term could be not equaled to an integer multiple of frequency,must modify the follow code
     */
    fun calculatePeriods(loanTerm: LoanTermType, frequency: RepaymentFrequency): Int {
        return loanTerm.term.calBetweenMultiple(frequency.term)
    }

    fun getPeriodDates(
        fromDateTime: Instant,
        toDateTime: Instant,
        frequency: RepaymentFrequency,
        repaymentDayType: RepaymentDayType = RepaymentDayType.BASE_LOAN_DAY,
        withDay: Instant? = null
    ): MutableList<Instant> {
        return when (repaymentDayType) {
            RepaymentDayType.BASE_LOAN_DAY -> getPeriodDatesWithDay(fromDateTime, toDateTime, frequency)
        }
    }

    fun getPeriodDatesWithDay(
        fromDateTime: Instant, toDateTime: Instant, frequency: RepaymentFrequency
    ): MutableList<Instant> {
        val periodDates = mutableListOf<Instant>()
        if (fromDateTime.isAfter(toDateTime))
            return periodDates
        var d = frequency.term.calDate(fromDateTime)
        while (d.isBefore(toDateTime) || d.isEqual(toDateTime)) {
            periodDates.add(d)
            d = frequency.term.calDate(d)
        }
        return periodDates
    }
}