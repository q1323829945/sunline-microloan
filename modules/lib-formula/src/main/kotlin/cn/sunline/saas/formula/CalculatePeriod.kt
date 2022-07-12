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

    data class PeriodDate(val fromDateTime: DateTime, val toDateTime: DateTime, val isEnough: Boolean)

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
            RepaymentDayType.MONTH_FIRST_DAY -> getPeriodDatesByCustom(fromDateTime, toDateTime, frequency, 1)
            RepaymentDayType.MONTH_LAST_DAY -> getPeriodDatesByCustom(fromDateTime, toDateTime, frequency, 31)

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
            periodDates.add(PeriodDate(lastDate, nextDate, true))
            lastDate = nextDate
            nextDate = frequency.term.calDate(nextDate)
        }
        return periodDates
    }

    fun getPeriodDatesByCustom(
        fromDateTime: DateTime, toDateTime: DateTime, frequency: RepaymentFrequency, withDay: Int
    ): MutableList<PeriodDate> {
        val periodDates = mutableListOf<PeriodDate>()
        if (fromDateTime.isAfter(toDateTime))
            return periodDates
        var lastDate = fromDateTime
        var nextDateTemp = frequency.term.calDate(lastDate)
        var nextDate = adjustRepaymentDateTime(lastDate, nextDateTemp, withDay)
        var isEnough = fromDateTime.dayOfMonth == withDay//nextDateTemp.isEqual(nextDate)
        while ((nextDate.isBefore(toDateTime) || nextDate.isEqual(toDateTime))) {
            periodDates.add(PeriodDate(lastDate, nextDate, isEnough))
            lastDate = nextDate
            nextDateTemp = frequency.term.calDate(nextDate)
            nextDate = adjustRepaymentDateTime(lastDate, nextDateTemp, withDay)
            isEnough = true//nextDateTemp.isEqual(nextDate)
        }
        if (nextDate.isAfter(toDateTime) && !lastDate.isEqual(toDateTime)) {
            periodDates.add(PeriodDate(lastDate, toDateTime, false))
        }
        return periodDates
    }


    fun adjustRepaymentDateTime(fromDateTime: DateTime, toDateTime: DateTime, repaymentDay: Int): DateTime {
        val fromDayOfMonth = fromDateTime.dayOfMonth
        val toDayOfMonth = toDateTime.dayOfMonth
        val toMonthOfYear = toDateTime.monthOfYear
        val fromMaxDay = fromDateTime.dayOfMonth().withMaximumValue().dayOfMonth
        val toMaxDay = toDateTime.dayOfMonth().withMaximumValue().dayOfMonth
        return if (toDayOfMonth < repaymentDay) {

            if (fromDayOfMonth == fromMaxDay) {
                if (repaymentDay < toMaxDay) toDateTime.withDayOfMonth(repaymentDay)
                else toDateTime.withDayOfMonth(toMaxDay)
            } else {
                val lastDate = toDateTime.plusMonths(-1)
                val lastDateMaxDay = lastDate.dayOfMonth().withMaximumValue().dayOfMonth
                if (repaymentDay < lastDateMaxDay) lastDate.withDayOfMonth(repaymentDay)
                else lastDate.withDayOfMonth(lastDateMaxDay)
            }
        } else if (toDayOfMonth > repaymentDay) {
            toDateTime.withDayOfMonth(repaymentDay)
        } else {
            toDateTime
        }
    }

    fun getRepaymentDateTime(fromDateTime: DateTime, repaymentDay: Int): DateTime {
        val maxDay = fromDateTime.dayOfMonth().withMaximumValue().dayOfMonth
        val dayOfMonth = fromDateTime.dayOfMonth
        return if (dayOfMonth < repaymentDay) {
            if (repaymentDay < maxDay) fromDateTime.withDayOfMonth(repaymentDay)
            else fromDateTime.withDayOfMonth(maxDay)
        } else {
            fromDateTime.withDayOfMonth(repaymentDay)
        }
    }
}