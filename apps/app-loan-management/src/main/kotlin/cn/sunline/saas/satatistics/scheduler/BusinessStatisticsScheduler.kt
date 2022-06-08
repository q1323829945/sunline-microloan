package cn.sunline.saas.satatistics.scheduler

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.statistics.modules.dto.*
import cn.sunline.saas.statistics.services.BusinessDetailService
import cn.sunline.saas.statistics.services.BusinessStatisticsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
@EnableScheduling
class BusinessStatisticsScheduler(
    private val tenantDateTime: TenantDateTime,
    private val tenantService: TenantService
):BaseScheduler(tenantDateTime,tenantService){
    @Autowired
    private lateinit var businessDetailService: BusinessDetailService

    @Autowired
    private lateinit var businessStatisticsService: BusinessStatisticsService

    //每年
    @Scheduled(cron = "0 0 0 1 1 ?")
    fun schedulerYearOfBusiness(){
        val tenantList = getTenantIdList()
        tenantList.forEach {
            ContextUtil.setTenant(it.id.toString())
            val endDate = getNowDate()
            val startDate = endDate.plusYears(-1)
            schedulerBusiness(startDate.toDate(),endDate.toDate(), Frequency.Y)
        }
    }
    //每月
    @Scheduled(cron = "0 0 0 1 * ?")
    fun schedulerMonthOfBusiness(){
        val tenantList = getTenantIdList()
        tenantList.forEach {
            ContextUtil.setTenant(it.id.toString())
            val endDate = getNowDate()
            val startDate = endDate.plusMonths(-1)
            schedulerBusiness(startDate.toDate(),endDate.toDate(), Frequency.M)
        }
    }

    //每日
    @Scheduled(cron = "0 0 0 * * ?")
    fun schedulerDayOfBusiness(){
        val tenantList = getTenantIdList()
        tenantList.forEach {
            ContextUtil.setTenant(it.id.toString())
            //TODO:再想想怎么处理各时区的统计信息
            val endDate = getNowDate()
            val startDate = endDate.plusDays(-1)
            schedulerBusiness(startDate.toDate(),endDate.toDate(), Frequency.D)
        }
    }

    private fun schedulerBusiness(startDate: Date, endDate: Date, frequency: Frequency){
        val customer = businessDetailService.getGroupByBusinessCount(DTOBusinessDetailQueryParams(startDate,endDate))
        customer.forEach {
            businessStatisticsService.saveBusinessStatistics(
                DTOBusinessStatistics(
                    customerId = it.customerId,
                    amount = it.amount,
                    currencyType = it.currency,
                    frequency = frequency
                )
            )
        }
    }

}