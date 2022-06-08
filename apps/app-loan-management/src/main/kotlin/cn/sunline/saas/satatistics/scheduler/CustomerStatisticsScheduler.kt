package cn.sunline.saas.satatistics.scheduler

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.statistics.modules.dto.DTOCustomerDetailQueryParams
import cn.sunline.saas.statistics.modules.dto.DTOCustomerStatistics
import cn.sunline.saas.statistics.services.CustomerDetailService
import cn.sunline.saas.statistics.services.CustomerStatisticsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
@EnableScheduling
class CustomerStatisticsScheduler(
    private val tenantDateTime: TenantDateTime,
    private val tenantService: TenantService
):BaseScheduler(tenantDateTime,tenantService){
    @Autowired
    private lateinit var customerDetailService: CustomerDetailService

    @Autowired
    private lateinit var customerStatisticsService: CustomerStatisticsService

    //每年
    @Scheduled(cron = "0 0 0 1 1 ?")
    fun schedulerYearOfCustomer(){
        val tenantList = getTenantIdList()
        tenantList.forEach {
            ContextUtil.setTenant(it.id.toString())
            val endDate = getNowDate()
            val startDate = endDate.plusYears(-1)
            schedulerCustomer(startDate.toDate(),endDate.toDate(), Frequency.Y)
        }
    }
    //每月
    @Scheduled(cron = "0 0 0 1 * ?")
    fun schedulerMonthOfCustomer(){
        val tenantList = getTenantIdList()
        tenantList.forEach {
            ContextUtil.setTenant(it.id.toString())
            val endDate = getNowDate()
            val startDate = endDate.plusMonths(-1)
            schedulerCustomer(startDate.toDate(),endDate.toDate(), Frequency.M)
        }
    }

    //每日
    @Scheduled(cron = "0 0 0 * * ?")
    fun schedulerDayOfCustomer(){
        val tenantList = getTenantIdList()
        tenantList.forEach {
            ContextUtil.setTenant(it.id.toString())
            //TODO:再想想怎么处理各时区的统计信息
            val endDate = getNowDate()
            val startDate = endDate.plusDays(-1)
            schedulerCustomer(startDate.toDate(),endDate.toDate(), Frequency.D)
        }
    }

    private fun schedulerCustomer(startDate: Date, endDate: Date, frequency: Frequency){
        val customer = customerDetailService.getGroupByCustomerCount(DTOCustomerDetailQueryParams(startDate,endDate))
        customer.forEach {
            customerStatisticsService.saveCustomerStatistics(
                DTOCustomerStatistics(
                    tenantId = it.tenantId,
                    partyCount = it.partyCount,
                    personCount = it.personCount,
                    organisationCount = it.organisationCount,
                    frequency = frequency
                )
            )
        }
    }

}