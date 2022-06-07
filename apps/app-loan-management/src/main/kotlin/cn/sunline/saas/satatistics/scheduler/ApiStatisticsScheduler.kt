package cn.sunline.saas.satatistics.scheduler

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.statistics.modules.dto.DTOApiDetailQueryParams
import cn.sunline.saas.statistics.modules.dto.DTOApiStatistics
import cn.sunline.saas.statistics.services.ApiDetailService
import cn.sunline.saas.statistics.services.ApiStatisticsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.Date

@Component
@EnableScheduling
class ApiStatisticsScheduler(
    private val tenantDateTime: TenantDateTime,
    private val tenantService: TenantService
):BaseScheduler(tenantDateTime,tenantService) {
    @Autowired
    private lateinit var apiDetailService: ApiDetailService

    @Autowired
    private lateinit var apiStatisticsService: ApiStatisticsService

    //每年
    @Scheduled(cron = "0 0 0 1 1 ?")
    fun schedulerYearOfApi(){
        val tenantList = getTenantIdList()
        tenantList.forEach {
            ContextUtil.setTenant(it.id.toString())
            val endDate = getNowDate()
            val startDate = endDate.plusYears(-1)
            schedulerApi(startDate.toDate(),endDate.toDate(),Frequency.Y,it)
        }
    }
    //每月
    @Scheduled(cron = "0 0 0 1 * ?")
    fun schedulerMonthOfApi(){
        val tenantList = getTenantIdList()
        tenantList.forEach {
            ContextUtil.setTenant(it.id.toString())
            val endDate = getNowDate()
            val startDate = endDate.plusMonths(-1)
            schedulerApi(startDate.toDate(),endDate.toDate(),Frequency.M,it)
        }
    }

    //每日
    @Scheduled(cron = "0 0 0 * * ?")
    fun schedulerDayOfApi(){
        val tenantList = getTenantIdList()
        tenantList.forEach {
            //TODO:再想想怎么处理各时区的统计信息
            ContextUtil.setTenant(it.id.toString())
            val endDate = getNowDate()
            val startDate = endDate.plusDays(-1)
            schedulerApi(startDate.toDate(),endDate.toDate(),Frequency.D,it)
        }
    }

    private fun schedulerApi(startDate:Date,endDate: Date,frequency: Frequency,tenant:Tenant){
        val api = apiDetailService.getGroupByApiCount(DTOApiDetailQueryParams(startDate,endDate,tenant.id))
        api.forEach {
            apiStatisticsService.saveApiStatistics(DTOApiStatistics(
                api = it.api,
                count = it.count,
                frequency = frequency
            ))
        }
    }


}