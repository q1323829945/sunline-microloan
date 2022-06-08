package cn.sunline.saas.satatistics.scheduler

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.statistics.modules.db.ApiStatistics
import cn.sunline.saas.statistics.modules.dto.DTOApiDetailQueryParams
import cn.sunline.saas.statistics.modules.dto.DTOApiStatistics
import cn.sunline.saas.statistics.modules.dto.DTOApiStatisticsFindParams
import cn.sunline.saas.statistics.services.ApiDetailService
import cn.sunline.saas.statistics.services.ApiStatisticsService
import org.joda.time.DateTime
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

    //每小时调度一次
    @Scheduled(cron = "30 * * * * ?")
    fun runApiScheduler(){
        runScheduler()
    }

    override fun saveYear(dateTime: DateTime){
        val api = checkApiExist(dateTime, Frequency.Y)
        api?:run {
            val endDate = getLocalDate(dateTime).plusDays(1)
            val startDate = endDate.plusYears(-1)
            schedulerApi(startDate.toDate(),endDate.toDate(),Frequency.Y)
        }
    }

    override fun saveMonth(dateTime: DateTime){
        val api = checkApiExist(dateTime, Frequency.M)
        api?:run {
            val endDate = getLocalDate(dateTime).plusDays(1)
            val startDate = endDate.plusMonths(-1)
            schedulerApi(startDate.toDate(),endDate.toDate(),Frequency.M)
        }
    }

    override fun saveDay(dateTime: DateTime){
        val api = checkApiExist(dateTime, Frequency.D)
        api?:run {
            val endDate = getLocalDate(dateTime).plusDays(1)
            val startDate = endDate.plusDays(-1)
            schedulerApi(startDate.toDate(),endDate.toDate(),Frequency.D)
        }
    }

    private fun checkApiExist(dateTime: DateTime,frequency: Frequency):ApiStatistics?{
        return apiStatisticsService.findByDate(DTOApiStatisticsFindParams(dateTime,frequency))
    }

    private fun schedulerApi(startDate:Date,endDate: Date,frequency: Frequency){
        val api = apiDetailService.getGroupByApiCount(DTOApiDetailQueryParams(startDate,endDate))
        api.forEach {
            apiStatisticsService.saveApiStatistics(DTOApiStatistics(
                api = it.api,
                count = it.count,
                frequency = frequency
            ))
        }
    }
}