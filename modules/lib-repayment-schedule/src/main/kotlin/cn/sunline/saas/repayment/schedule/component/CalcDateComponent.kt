



























package cn.sunline.saas.repayment.schedule.component

import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleResetResult
import org.joda.time.DateTime


object CalcDateComponent {

    fun calcNextRepaymentDateTime(startDateTime: DateTime, endDateTime: DateTime, nextRepaymentDateTime: DateTime): DateTime{
        return if(nextRepaymentDateTime > startDateTime && nextRepaymentDateTime <= endDateTime){
            nextRepaymentDateTime
        }else if (nextRepaymentDateTime <= startDateTime){
            nextRepaymentDateTime.plusMonths(1)
        } else{
            endDateTime
        }
    }



    fun calcResetNextRepaymentDateInfo(repaymentDateTime:DateTime,repaymentScheduleDetail: MutableList<RepaymentScheduleDetail>): DTORepaymentScheduleResetResult {
        var flag = 1
        var currentPeriod = 0
        var nextPeriod = 0
        var remainPeriods = 0
        var nextRepaymentDateTime = repaymentDateTime
        run loop@{
            repaymentScheduleDetail?.forEach{
                val currentDateTime = DateTime(it.repaymentDate)
                val currentYer = currentDateTime.year()
                val currentMonth = currentDateTime.monthOfYear().get()
                val currentDay = currentDateTime.dayOfMonth().get()
                val repaymentYer = repaymentDateTime.year()
                val repaymentMonth = repaymentDateTime.monthOfYear().get()
                val repaymentDay = repaymentDateTime.dayOfMonth().get()

                if(currentYer ==repaymentYer && currentMonth == repaymentMonth){
                    if(repaymentDay>=currentDay){
                        currentPeriod = it.period + 1
                        nextPeriod = currentPeriod +1
                        remainPeriods = remainPeriods?.minus(it.period)
                    }else {
                        nextRepaymentDateTime = DateTime(it.repaymentDate)
                        currentPeriod = it.period
                        remainPeriods = remainPeriods?.minus(it.period)?.plus(1)
                        return@loop
                    }
                }

                if(flag == nextPeriod-1){
                    nextRepaymentDateTime = DateTime(it.repaymentDate)
                    return@loop
                }

                flag += 1
            }
        }
        return DTORepaymentScheduleResetResult(currentPeriod,nextPeriod,remainPeriods,nextRepaymentDateTime.toDate())
    }

    fun calcRepaymentDay(repaymentDateTime:DateTime,repaymentScheduleDetail: MutableList<RepaymentScheduleDetail>): Int {
        var flag = 1
        var day = 0
        var nextPeriod = 0
        run loop@{
            repaymentScheduleDetail?.forEach{
                val currentDateTime = DateTime(it.repaymentDate)
                val currentYer = currentDateTime.year()
                val currentMonth = currentDateTime.monthOfYear().get()
                val currentDay = currentDateTime.dayOfMonth().get()
                val repaymentYer = repaymentDateTime.year()
                val repaymentMonth = repaymentDateTime.monthOfYear().get()
                val repaymentDay = repaymentDateTime.dayOfMonth().get()

                if(currentYer ==repaymentYer && currentMonth == repaymentMonth){
                    if(repaymentDay>=currentDay){
                        nextPeriod = it.period + 2
                    }else {
                        day = DateTime(it.repaymentDate).dayOfMonth
                        return@loop
                    }
                }
                if(flag == nextPeriod-1){
                    day = DateTime(it.repaymentDate).dayOfMonth
                    return@loop
                }
                flag += 1
            }
        }
        return day
    }

}