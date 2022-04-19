

package cn.sunline.saas.repayment.schedule.component

import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import org.joda.time.DateTime
import org.joda.time.Instant
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

object CalcPeriodComponent {

    fun calcDuePeriods(startDateInstant: Instant, endDateInstant: Instant, repaymentDay: Int, repaymentFrequency: RepaymentFrequency): Int{
        val startDateTime = startDateInstant.toDateTime()
        val endDateTime = endDateInstant.toDateTime()
        val result = (startDateTime.year - endDateTime.year) * 12 + startDateTime.monthOfYear().get() - endDateTime.monthOfYear().get()
        var monthSpace = if (result == 0) 1 else abs(result)
        val startDay = startDateTime.dayOfMonth().get()
        val endDay = startDateTime.dayOfMonth().get()
        monthSpace = if((startDay == repaymentDay && endDay <= repaymentDay)) monthSpace  else monthSpace + 1
        val months = repaymentFrequency.months
        val divideAndRemainder = BigDecimal(monthSpace).divideAndRemainder(BigDecimal(months))
        // TODO 频次大于期数 异常处理
        return if(divideAndRemainder[1].toInt()>0) divideAndRemainder[0].toInt() + 1 else divideAndRemainder[0].toInt()
    }

    fun calcRemainPeriods(
        repaymentDate: Instant,
        repaymentScheduleDetail: MutableList<RepaymentScheduleDetail>
    ): Array<Int> {
        repaymentScheduleDetail.sortBy { it.period }
        var periods = 0;
        var currentPeriod = 0;
        var finalPeriod = 0
        var firstFlag = false
        val repaymentDateTime = repaymentDate.toDateTime()
        for (detail in repaymentScheduleDetail) {
            val dateTime = detail.repaymentDate
            if (dateTime > repaymentDateTime) {
                if (!firstFlag) {
                    currentPeriod = detail.period
                }
                firstFlag = true
                periods++;
            }
            finalPeriod++
        }
        return arrayOf(currentPeriod, finalPeriod, periods)
    }

}