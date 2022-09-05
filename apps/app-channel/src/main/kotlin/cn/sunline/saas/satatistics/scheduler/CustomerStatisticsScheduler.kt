package cn.sunline.saas.satatistics.scheduler

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.statistics.modules.db.CustomerStatistics
import cn.sunline.saas.channel.statistics.modules.dto.DTOCustomerDetailQueryParams
import cn.sunline.saas.channel.statistics.modules.dto.DTOCustomerStatistics
import cn.sunline.saas.channel.statistics.modules.dto.DTOCustomerStatisticsFindParams
import cn.sunline.saas.channel.statistics.services.CustomerDetailService
import cn.sunline.saas.channel.statistics.services.CustomerStatisticsService
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

//@Component
//@EnableScheduling
class CustomerStatisticsScheduler(
    private val tenantDateTime: TenantDateTime,
    private val tenantService: TenantService
):BaseScheduler(tenantDateTime,tenantService){
    @Autowired
    private lateinit var customerDetailService: CustomerDetailService

    @Autowired
    private lateinit var customerStatisticsService: CustomerStatisticsService

    //每小时调度一次
    @Scheduled(cron = "0 0 * * * ?")
    fun runCustomerScheduler(){
        runScheduler()
    }

    override fun saveYear(dateTime: DateTime){
        val api = checkCustomerExist(dateTime, Frequency.Y)
        api?:run {
            val endDate = getLocalDate(dateTime)
            val startDate = endDate.plusYears(-1)
            schedulerCustomer(startDate.toDate(),endDate.toDate(),Frequency.Y)
        }
    }

    override fun saveMonth(dateTime: DateTime){
        val api = checkCustomerExist(dateTime, Frequency.M)
        api?:run {
            val endDate = getLocalDate(dateTime)
            val startDate = endDate.plusMonths(-1)
            schedulerCustomer(startDate.toDate(),endDate.toDate(),Frequency.M)
        }
    }

    override fun saveDay(dateTime: DateTime){
        val api = checkCustomerExist(dateTime, Frequency.D)
        api?:run {
            val endDate = getLocalDate(dateTime)
            val startDate = endDate.plusDays(-1)
            schedulerCustomer(startDate.toDate(),endDate.toDate(),Frequency.D)
        }
    }

    private fun checkCustomerExist(dateTime: DateTime, frequency: Frequency): CustomerStatistics?{
        return customerStatisticsService.findByDate(DTOCustomerStatisticsFindParams(dateTime,frequency))
    }

    private fun schedulerCustomer(startDate: Date, endDate: Date, frequency: Frequency){
        val customer = customerDetailService.getGroupByCustomerCount(DTOCustomerDetailQueryParams(startDate,endDate))
        customer.forEach {
            customerStatisticsService.saveCustomerStatistics(
                DTOCustomerStatistics(
                    partyCount = it.partyCount,
                    personCount = it.personCount,
                    organisationCount = it.organisationCount,
                    frequency = frequency
                )
            )
        }
    }

}