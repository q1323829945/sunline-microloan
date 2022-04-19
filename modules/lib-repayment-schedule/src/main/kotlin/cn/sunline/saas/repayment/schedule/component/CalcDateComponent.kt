



























package cn.sunline.saas.repayment.schedule.component

import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleResetResult
import org.joda.time.DateTime
import org.joda.time.Instant
import org.joda.time.format.DateTimeFormat


object CalcDateComponent {

    private val format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    fun calcNextRepaymentDateTime(startDateTime: DateTime, endDateTime: DateTime, nextRepaymentDateTime: DateTime): DateTime{
        return if(nextRepaymentDateTime > startDateTime && nextRepaymentDateTime <= endDateTime){
            nextRepaymentDateTime
        }else if (nextRepaymentDateTime <= startDateTime){
            nextRepaymentDateTime.plus(1)
        } else{
            endDateTime
        }
    }

    fun formatInstantToView(instant: Instant): String{
        return instant.toString(format)
    }

    fun parseViewToInstant(instantString: String): Instant{
        return Instant.parse(instantString,format)
    }
}