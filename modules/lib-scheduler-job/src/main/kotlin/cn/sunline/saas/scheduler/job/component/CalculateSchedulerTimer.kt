package cn.sunline.saas.scheduler.job.component

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.job.exception.SchedulerTimerNotConfigureException
import cn.sunline.saas.scheduler.job.service.SchedulerTimerService
import org.joda.time.DateTime
import org.springframework.stereotype.Component

/**
 * @title: CalculateTime
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 13:48
 */
@Component
class CalculateSchedulerTimer(
    private val schedulerTimerService: SchedulerTimerService,
    private val tenantDateTime: TenantDateTime
) {

    fun baseDateTime(current:DateTime = tenantDateTime.now()): DateTime {
        println(current)
        val switchDayTime = schedulerTimerService.getOne(ContextUtil.getTenant().toLong())?.switchDayTime
        if (switchDayTime == null) {
            throw SchedulerTimerNotConfigureException("switch daytime has not configured")
        } else {
            val baseDateTime = current.withHourOfDay(switchDayTime).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
            return if (baseDateTime.isBefore(current)){
                baseDateTime.plusDays(1)
            }else{
                baseDateTime
            }
        }
    }
}