package cn.sunline.saas.satatistics.scheduler

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.statistics.modules.db.CommissionStatistics
import cn.sunline.saas.channel.statistics.modules.db.LoanApplicationStatistics
import cn.sunline.saas.channel.statistics.modules.dto.*
import cn.sunline.saas.channel.statistics.services.CommissionDetailService
import cn.sunline.saas.channel.statistics.services.CommissionStatisticsService
import cn.sunline.saas.channel.statistics.services.LoanApplicationDetailService
import cn.sunline.saas.channel.statistics.services.LoanApplicationStatisticsService
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

//@Component
//@EnableScheduling
class CommissionStatisticsScheduler(
    private val tenantDateTime: TenantDateTime,
    private val tenantService: TenantService
) : BaseScheduler(tenantDateTime, tenantService) {

    private val ratio = BigDecimal(0.2).setScale(2, RoundingMode.HALF_UP)

    @Autowired
    private lateinit var commissionStatisticsService: CommissionStatisticsService

    @Autowired
    private lateinit var commissionDetailService: CommissionDetailService

    //每小时调度一次
    @Scheduled(cron = "0 0 * * * ?")
    fun runLoanApplicationScheduler() {
        runScheduler()
    }

    override fun saveYear(dateTime: DateTime) {
        val endDate = getLocalDate(dateTime)
        val startDate = endDate.plusYears(-1)
        schedulerCommissionStatistics(dateTime, startDate.toDate(), endDate.toDate(), Frequency.Y)
    }

    override fun saveMonth(dateTime: DateTime) {
        val endDate = getLocalDate(dateTime)
        val startDate = endDate.plusMonths(-1)
        schedulerCommissionStatistics(dateTime, startDate.toDate(), endDate.toDate(),Frequency.M)
    }

    override fun saveDay(dateTime: DateTime) {
        val endDate = getLocalDate(dateTime)
        val startDate = endDate.plusDays(-1)
        schedulerCommissionStatistics(dateTime, startDate.toDate(), endDate.toDate(),Frequency.D)
    }

    private fun schedulerCommissionStatistics(dateTime: DateTime, startDate: Date, endDate: Date, frequency: Frequency) {
        val commission = commissionDetailService.getGroupByStatusCount(
            DTOCommissionDetailQueryParams(
                startDate,
                endDate
            )
        )
        commission.forEach {
            // TODO  get CommissionFeature by commissionFeatureId ,get the ratio
            val business = checkCommissionStatisticsExist(it.channelCode,it.channelName, dateTime, frequency)
            if (business != null) {
                business.amount = it.amount.multiply(ratio).setScale(2, RoundingMode.HALF_UP)
                business.statisticsAmount =  it.amount
                business.datetime = tenantDateTime.now().toDate()
                commissionStatisticsService.save(business)
            } else {
                commissionStatisticsService.saveCommissionStatistics(
                    DTOCommissionStatistics(
                        channelCode = it.channelCode,
                        channelName = it.channelName,
                        commissionFeatureId = 0,
                        statisticsAmount =  it.amount,
                        amount = it.amount.multiply(ratio).setScale(2, RoundingMode.HALF_UP),
                        frequency = frequency
                    )
                )
            }
        }
    }

    private fun checkCommissionStatisticsExist(
        channelCode: String,
        channelName: String,
        dateTime: DateTime,
        frequency: Frequency
    ): CommissionStatistics? {
        return commissionStatisticsService.findByDate(
            DTOCommissionStatisticsFindParams(
                channelCode = channelCode,
                channelName = channelName,
                dateTime = dateTime,
                frequency = frequency
            )
        )
    }
}