package cn.sunline.saas.formula

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import org.joda.time.Instant

/**
 * @title: CalculatePeriod
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/19 11:20
 */
object CalculatePeriod {

    data class PeriodDate(val fromDateTime: Instant,val toDateTime: Instant)

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
    ): MutableList<PeriodDate> {
        return when (repaymentDayType) {
            RepaymentDayType.BASE_LOAN_DAY -> getPeriodDatesByStandard(fromDateTime, toDateTime, frequency)
        }
    }

    fun getPeriodDatesByStandard(
        fromDateTime: Instant, toDateTime: Instant, frequency: RepaymentFrequency
    ): MutableList<PeriodDate> {
        val periodDates = mutableListOf<PeriodDate>()
        if (fromDateTime.isAfter(toDateTime))
            return periodDates
        var lastDate = fromDateTime
        var nextDate = frequency.term.calDate(fromDateTime)
        while (nextDate.isBefore(toDateTime) || nextDate.isEqual(toDateTime)) {
            periodDates.add(PeriodDate(lastDate,nextDate))
            lastDate = nextDate
            nextDate = frequency.term.calDate(nextDate)
        }
        return periodDates
    }
}