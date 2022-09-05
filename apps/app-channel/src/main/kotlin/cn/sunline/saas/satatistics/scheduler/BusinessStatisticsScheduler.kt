package cn.sunline.saas.satatistics.scheduler

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.statistics.modules.TransactionType
import cn.sunline.saas.channel.statistics.modules.db.BusinessStatistics
import cn.sunline.saas.channel.statistics.modules.dto.*
import cn.sunline.saas.channel.statistics.services.BusinessDetailService
import cn.sunline.saas.channel.statistics.services.BusinessStatisticsService
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

//@Component
//@EnableScheduling
class BusinessStatisticsScheduler(
    private val tenantDateTime: TenantDateTime,
    private val tenantService: TenantService
):BaseScheduler(tenantDateTime,tenantService){
    @Autowired
    private lateinit var businessDetailService: BusinessDetailService

    @Autowired
    private lateinit var businessStatisticsService: BusinessStatisticsService

    //每小时调度一次
    @Scheduled(cron = "0 0 * * * ?")
    fun runBusinessScheduler(){
        runScheduler()
    }

    override fun saveYear(dateTime: DateTime){
        val endDate = getLocalDate(dateTime)
        val startDate = endDate.plusYears(-1)
        schedulerBusiness(dateTime,startDate.toDate(),endDate.toDate(),Frequency.Y)
    }

    override fun saveMonth(dateTime: DateTime){
        val endDate = getLocalDate(dateTime)
        val startDate = endDate.plusMonths(-1)
        schedulerBusiness(dateTime,startDate.toDate(),endDate.toDate(),Frequency.M)
    }

    override fun saveDay(dateTime: DateTime){
        val endDate = getLocalDate(dateTime)
        val startDate = endDate.plusDays(-1)
        schedulerBusiness(dateTime,startDate.toDate(),endDate.toDate(),Frequency.D)
    }

    private fun schedulerBusiness(dateTime:DateTime,startDate: Date, endDate: Date, frequency: Frequency){
        val customer = businessDetailService.getGroupByBusinessCount(DTOBusinessDetailQueryParams(startDate,endDate))
        customer.forEach {
            val business = checkBusinessExist(it.customerId,dateTime,frequency,it.currency)
            business?:run {
                businessStatisticsService.saveBusinessStatistics(
                    DTOBusinessStatistics(
                        customerId = it.customerId,
                        paymentAmount = it.paymentAmount,
                        repaymentAmount = it.repaymentAmount,
                        currencyType = it.currency,
                        frequency = frequency
                    )
                )
            }
        }
    }

    private fun checkBusinessExist(customerId:Long, dateTime: DateTime, frequency: Frequency, currencyType: CurrencyType):BusinessStatistics?{
        return businessStatisticsService.findByDate(DTOBusinessStatisticsFindParams(customerId,dateTime,frequency,currencyType))
    }

}