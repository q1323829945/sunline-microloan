package cn.sunline.saas.satatistics.scheduler

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.statistics.modules.db.LoanApplicationStatistics
import cn.sunline.saas.channel.statistics.modules.dto.DTOLoanApplicationDetailQueryParams
import cn.sunline.saas.channel.statistics.modules.dto.DTOLoanApplicationStatistics
import cn.sunline.saas.channel.statistics.modules.dto.DTOLoanApplicationStatisticsFindParams
import cn.sunline.saas.channel.statistics.services.LoanApplicationDetailService
import cn.sunline.saas.channel.statistics.services.LoanApplicationStatisticsService
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
//@EnableScheduling
class LoanApplicationStatisticsScheduler(
    private val tenantDateTime: TenantDateTime,
    private val tenantService: TenantService
) : BaseScheduler(tenantDateTime, tenantService) {
    @Autowired
    private lateinit var loanApplicationDetailService: LoanApplicationDetailService

    @Autowired
    private lateinit var loanApplicationStatisticsService: LoanApplicationStatisticsService

    //每小时调度一次
    @Scheduled(cron = "0 0 * * * ?")
    fun runLoanApplicationScheduler() {
        runScheduler()
    }

    override fun saveYear(dateTime: DateTime) {
        val endDate = getLocalDate(dateTime)
        val startDate = endDate.plusYears(-1)
        schedulerLoanApplication(dateTime, startDate.toDate(), endDate.toDate(), Frequency.Y)
    }

    override fun saveMonth(dateTime: DateTime) {
        val endDate = getLocalDate(dateTime)
        val startDate = endDate.plusMonths(-1)
        schedulerLoanApplication(dateTime, startDate.toDate(), endDate.toDate(),Frequency.M)
    }

    override fun saveDay(dateTime: DateTime) {
        val endDate = getLocalDate(dateTime)
        val startDate = endDate.plusDays(-1)
        schedulerLoanApplication(dateTime, startDate.toDate(), endDate.toDate(),Frequency.D)
    }

    private fun schedulerLoanApplication(dateTime: DateTime, startDate: Date, endDate: Date, frequency: Frequency) {
        val loanApplication =
            loanApplicationDetailService.getGroupByStatusCount(DTOLoanApplicationDetailQueryParams(startDate, endDate))
        loanApplication.forEach {
            val business = checkLoanApplicationExist(it.channelCode, it.channelName,it.productId, dateTime, frequency)
            if (business != null) {
                business.amount = it.amount
                business.applyCount = it.applyCount
                business.approvalCount = it.approvalCount
                business.datetime = tenantDateTime.now().toDate()
                loanApplicationStatisticsService.save(business)
            } else {
                loanApplicationStatisticsService.saveLoanApplicationStatistics(
                    DTOLoanApplicationStatistics(
                        channelCode = it.channelCode,
                        channelName = it.channelName,
                        productId = it.productId,
                        productName = it.productName,
                        amount = it.amount,
                        applyCount = it.applyCount,
                        approvalCount = it.approvalCount,
                        frequency = frequency
                    )
                )
            }
        }
    }

    private fun checkLoanApplicationExist(
        channelCode: String,
        channelName: String,
        productId: Long,
        dateTime: DateTime,
        frequency: Frequency
    ): LoanApplicationStatistics? {
        return loanApplicationStatisticsService.findByDate(
            DTOLoanApplicationStatisticsFindParams(
                channelCode = channelCode,
                channelName = channelName,
                productId = productId,
                dateTime = dateTime,
                frequency = frequency
            )
        )
    }
}