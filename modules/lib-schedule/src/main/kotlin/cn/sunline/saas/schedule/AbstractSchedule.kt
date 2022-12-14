package cn.sunline.saas.schedule

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import org.joda.time.DateTime
import org.joda.time.Instant
import java.math.BigDecimal

/**
 * @title: AbstractSchedule
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/20 14:38
 */
abstract class AbstractSchedule(
    val amount: BigDecimal,
    val interestRateYear: BigDecimal,
    val term: LoanTermType,
    val frequency: RepaymentFrequency,
    val repaymentDayType: RepaymentDayType?,
    val baseYearDays: BaseYearDays,
    val fromDateTime: DateTime,
    toDateTime: DateTime?,
    val repaymentDateTime: DateTime?,
    val feeAmount: BigDecimal
) {
    var toDateTime: DateTime

    init {
        if(toDateTime == null){
            this.toDateTime = term.term.calDate(fromDateTime)
        }else{
            this.toDateTime = toDateTime
        }
    }

    abstract fun getSchedules(): MutableList<Schedule>
}