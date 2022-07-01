package cn.sunline.saas.formula

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import org.joda.time.DateTime
import org.joda.time.Instant

/**
 * @title: CalculatePeriod
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/19 11:20
 */
object CalculatePeriod {

    data class PeriodDate(val fromDateTime: DateTime, val toDateTime: DateTime)

    /**
     * for now,the term is equaled to an integer multiple of frequency
     * for future,the term could be not equaled to an integer multiple of frequency,must modify the follow code
     */
    fun calculatePeriods(loanTerm: LoanTermType, frequency: RepaymentFrequency): Int {
        return loanTerm.term.calBetweenMultiple(frequency.term)
    }

    fun getPeriodDates(
        fromDateTime: DateTime,
        toDateTime: DateTime,
        frequency: RepaymentFrequency,
        repaymentDayType: RepaymentDayType = RepaymentDayType.BASE_LOAN_DAY,
        withDay: Int? = null
    ): MutableList<PeriodDate> {
        return when (repaymentDayType) {
            RepaymentDayType.BASE_LOAN_DAY -> getPeriodDatesByStandard(fromDateTime, toDateTime, frequency)
            RepaymentDayType.CUSTOM_DAY -> getPeriodDatesByCustom(fromDateTime, toDateTime, frequency, withDay!!)

        }
    }

    fun getPeriodDatesByStandard(
        fromDateTime: DateTime, toDateTime: DateTime, frequency: RepaymentFrequency
    ): MutableList<PeriodDate> {
        val periodDates = mutableListOf<PeriodDate>()
        if (fromDateTime.isAfter(toDateTime))
            return periodDates
        var lastDate = fromDateTime
        var nextDate = frequency.term.calDate(fromDateTime)
        while (nextDate.isBefore(toDateTime) || nextDate.isEqual(toDateTime)) {
            periodDates.add(PeriodDate(lastDate, nextDate))
            lastDate = nextDate
            nextDate = frequency.term.calDate(nextDate)
        }
        return periodDates
    }

    private fun getPeriodDatesByCustom(
        fromDateTime: DateTime, toDateTime: DateTime, frequency: RepaymentFrequency, withDay: Int
    ): MutableList<PeriodDate> {
        val periodDates = mutableListOf<PeriodDate>()
        if (fromDateTime.isAfter(toDateTime))
            return periodDates
        val repaymentDateTime = getRepaymentDateTime(fromDateTime,withDay)
        var lastDate = if(repaymentDateTime.isBefore(fromDateTime)){
            val plusMonths = repaymentDateTime.plusMonths(1)
            periodDates.add(PeriodDate(fromDateTime, plusMonths))
            plusMonths
        }else if(repaymentDateTime.isAfter(fromDateTime)){
            periodDates.add(PeriodDate(fromDateTime, repaymentDateTime))
            repaymentDateTime
        }else{
            repaymentDateTime
        }
        var times = 1
        var nextDate = frequency.term.calDate(lastDate, times)
        while ((nextDate.isBefore(toDateTime) || nextDate.isEqual(toDateTime) && !nextDate.isEqual(toDateTime))) {
            periodDates.add(PeriodDate(lastDate, nextDate))
            lastDate = nextDate
            times++
            nextDate = frequency.term.calDate(repaymentDateTime, times)
        }

        if(lastDate.isBefore(toDateTime)&& !lastDate.isEqual(toDateTime)){
            periodDates.add(PeriodDate(lastDate, toDateTime))
        }
        return periodDates
    }

    fun getRepaymentDateTime(fromDateTime: DateTime, repaymentDay: Int): DateTime {
        val monthOfYear = fromDateTime.monthOfYear
        val dayOfMonth = fromDateTime.dayOfMonth
        val monthList = listOf<Int>(4, 6, 9, 11)
        var repaymentDateTime = fromDateTime
        if (monthList.contains(monthOfYear)) {
            repaymentDateTime = if (repaymentDay == 31) {
                fromDateTime.plusMonths(1).withDayOfMonth(repaymentDay)
            } else {
                fromDateTime.withDayOfMonth(repaymentDay)
            }
        } else if (monthOfYear == 2) {
            repaymentDateTime = if ((dayOfMonth == 28 && dayOfMonth < repaymentDay)
                || (dayOfMonth == 29 && dayOfMonth < repaymentDay)
            ) {
                fromDateTime.plusMonths(1).withDayOfMonth(repaymentDay)
            } else {
                fromDateTime.withDayOfMonth(repaymentDay)
            }
        }else{
            repaymentDateTime = fromDateTime.withDayOfMonth(repaymentDay)
        }
        return repaymentDateTime
    }
}