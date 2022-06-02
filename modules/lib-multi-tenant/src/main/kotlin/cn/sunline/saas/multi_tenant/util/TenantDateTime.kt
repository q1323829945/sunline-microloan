package cn.sunline.saas.multi_tenant.util

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getTimeZone
import cn.sunline.saas.global.util.setTimeZone
import cn.sunline.saas.multi_tenant.exception.TenantNotFoundException
import cn.sunline.saas.multi_tenant.services.TenantService
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Interval
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import org.springframework.stereotype.Component
import java.util.*

/**
 * @title: DateTime
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/24 9:13
 */
@Component
class TenantDateTime(private val tenantService: TenantService) {

    fun toTenantDateTime(dt: Date): DateTime {
        return DateTime(dt, getTenantTimeZone())
    }

    fun toTenantDateTime(dt: String): DateTime {
        return DateTime(dt, getTenantTimeZone())
    }

    fun now(): DateTime {
        return DateTime.now(getTenantTimeZone())
    }

    fun getYearMonthDay(dt: DateTime): String {
        return dt.toString(DateTimeFormat.forPattern("yyyyMMdd"))
    }
    fun betweenTimes(end: DateTime, start: DateTime = now()): Period {
        return Interval(start,end).toPeriod()
    }

    private fun getTenantTimeZone(): DateTimeZone {
        var timeZone = ContextUtil.getTimeZone()
        if (timeZone == null) {
            val tenant = tenantService.getOne(ContextUtil.getTenant().toLong())
            if (tenant == null) {
                throw TenantNotFoundException("tenant not found")
            } else {
                timeZone = DateTimeZone.forID(tenant.country.datetimeZone)
                ContextUtil.setTimeZone(timeZone)
                return timeZone
            }
        } else {
            return timeZone
        }
    }

}