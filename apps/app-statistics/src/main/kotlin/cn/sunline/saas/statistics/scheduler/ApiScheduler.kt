package cn.sunline.saas.statistics.scheduler

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.statistics.modules.dto.DTOApiDetailQueryDate
import cn.sunline.saas.statistics.modules.dto.DTOApiStatistics
import cn.sunline.saas.statistics.services.ApiDetailService
import cn.sunline.saas.statistics.services.ApiStatisticsService
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.Date

@Component
@EnableScheduling
class ApiScheduler {
    @Autowired
    private lateinit var apiDetailService: ApiDetailService

    @Autowired
    private lateinit var apiStatisticsService: ApiStatisticsService

    @Autowired
    private lateinit var tenantService: TenantService

    //每年
    @Scheduled(cron = "0 0 0 1 1 ?")
    fun schedulerYearOfApi(){
        val tenantList = getTenantIdList()
        tenantList.forEach {
            if(DateTimeZone.getDefault() == DateTimeZone.forID(it.country.datetimeZone)){
                val endDate = getNowDate(DateTimeZone.forID(it.country.datetimeZone))
                val startDate = endDate.plusYears(-1)
                schedulerApi(startDate.toDate(),endDate.toDate(),Frequency.Y,it)
            }
        }
    }
    //每月
    @Scheduled(cron = "0 0 0 1 * ?")
    fun schedulerMonthOfApi(){
        val tenantList = getTenantIdList()
        tenantList.forEach {
            if(DateTimeZone.getDefault() == DateTimeZone.forID(it.country.datetimeZone)){
                val endDate = getNowDate(DateTimeZone.forID(it.country.datetimeZone))
                val startDate = endDate.plusMonths(-1)
                schedulerApi(startDate.toDate(),endDate.toDate(),Frequency.M,it)
            }
        }
    }

    //每日
    @Scheduled(cron = "0 0 0 * * ?")
    fun schedulerDayOfApi(){
        val tenantList = getTenantIdList()
        tenantList.forEach {
            //TODO:再想想怎么处理各时区的统计信息
            if(DateTimeZone.getDefault() == DateTimeZone.forID(it.country.datetimeZone)){
                val endDate = getNowDate(DateTimeZone.forID(it.country.datetimeZone))
                val startDate = endDate.plusDays(-1)
                schedulerApi(startDate.toDate(),endDate.toDate(),Frequency.D,it)
            }
        }
    }

    private fun getNowDate(dateTimeZone: DateTimeZone):DateTime{
        return DateTime(DateTime.now(dateTimeZone).toLocalDate().toDate(),dateTimeZone)
    }


    private fun schedulerApi(startDate:Date,endDate: Date,frequency: Frequency,tenant:Tenant){
        val api = apiDetailService.getGroupByApiCount(DTOApiDetailQueryDate(startDate,endDate,tenant.id))
        api.forEach {
            apiStatisticsService.saveApiStatistics(DTOApiStatistics(
                api = it.api,
                count = it.count,
                frequency = frequency
            ))
        }
    }

    private fun getTenantIdList():List<Tenant>{
        return tenantService.getPaged(null, Pageable.unpaged()).content
    }

}