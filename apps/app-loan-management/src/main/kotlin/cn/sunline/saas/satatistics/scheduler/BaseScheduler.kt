package cn.sunline.saas.satatistics.scheduler

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import org.joda.time.DateTime
import org.springframework.data.domain.Pageable

abstract class BaseScheduler(
    private val tenantDateTime: TenantDateTime,
    private val tenantService: TenantService
) {
    fun runScheduler(){
        val tenantList = getTenantList()
        tenantList.forEach {
            ContextUtil.setTenant(it.id.toString())
            val nowDate = tenantDateTime.now()
            //根据租户时区统计数据
            //每日统计
            if(nowDate.hourOfDay == 0){
                saveDay(nowDate)

                //每月统计
                if(nowDate.dayOfMonth == 1){
                    saveMonth(nowDate)

                    //每年统计
                    if(nowDate.monthOfYear == 1){
                        saveYear(nowDate)
                    }
                }
            }
        }
    }


    abstract fun saveYear(dateTime: DateTime)

    abstract fun saveMonth(dateTime: DateTime)

    abstract fun saveDay(dateTime: DateTime)

    fun getLocalDate(dateTime: DateTime): DateTime {
        return tenantDateTime.toTenantDateTime(dateTime.toLocalDate().toDate())
    }

    private fun getTenantList():List<Tenant>{
        return tenantService.getPaged(null, Pageable.unpaged()).content
    }
}