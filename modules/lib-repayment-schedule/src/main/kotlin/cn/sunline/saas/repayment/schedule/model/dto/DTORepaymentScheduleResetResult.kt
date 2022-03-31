package cn.sunline.saas.repayment.schedule.model.dto

import java.util.*


data class DTORepaymentScheduleResetResult (
    var currentPeriod:Int,
    var nextPeriod:Int,
    var remainPeriods:Int,
    var nextRepaymentDate: Date,
)