package cn.sunline.saas.satatistics.service

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.satatistics.service.dto.DTOLoanApplicationStatisticsCount
import cn.sunline.saas.statistics.modules.db.LoanApplicationStatistics
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationDetail
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationDetailQueryParams
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationStatistics
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationStatisticsFindParams
import cn.sunline.saas.statistics.services.LoanApplicationDetailService
import cn.sunline.saas.statistics.services.LoanApplicationStatisticsService
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class LoanApplicationStatisticsManagerService(
    private val tenantDateTime: TenantDateTime
) {

    @Autowired
    private lateinit var loanApplicationStatisticsService: LoanApplicationStatisticsService

    @Autowired
    private lateinit var loanApplicationDetailService: LoanApplicationDetailService

    fun getPaged(
        year: Long?,
        month: Long?,
        day: Long?,
        tenantId: Long?,
        pageable: Pageable
    ): Page<DTOLoanApplicationStatisticsCount> {
        return loanApplicationStatisticsService.getPaged(year, month, day, tenantId, pageable).map {
            DTOLoanApplicationStatisticsCount(
                dateTime = tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(it.datetime)),
                channel = it.channel,
                productId = it.productId.toString(),
                productName = it.productName,
                amount = it.amount,
                applyCount = it.applyCount,
                approvalCount = it.approvalCount
            )
        }
    }

//    fun addLoanApplicationDetailAndStatistics(dtoLoanApplicationDetail: DTOLoanApplicationDetail) {
//        val dateTime = tenantDateTime.now()
//        val endDate = tenantDateTime.toTenantDateTime(dateTime.toLocalDate().toDate())
//        val startDate = endDate.plusDays(-1)
//        saveLoanApplicationDetail(dtoLoanApplicationDetail)
//        saveLoanApplicationStatistics(dateTime, startDate.toDate(), endDate.toDate(), Frequency.D)
//    }

    fun addLoanApplicationDetail(dtoLoanApplicationDetail: DTOLoanApplicationDetail) {
        loanApplicationDetailService.getByApplicationId(dtoLoanApplicationDetail.applicationId)?: run{
            loanApplicationDetailService.saveApplicationDetail(dtoLoanApplicationDetail)
        }
    }

    fun addLoanApplicationStatistics() {
        val dateTime = tenantDateTime.now()
        val startDate = tenantDateTime.toTenantDateTime(dateTime.toLocalDate().toDate())
        val endDate = startDate.plusDays(1)
        val loanApplication =
            loanApplicationDetailService.getGroupByStatusCount(DTOLoanApplicationDetailQueryParams(startDate.toDate(), endDate.toDate()))
        loanApplication.forEach {
            val business = checkLoanApplicationStatisticsExist(it.channel, it.productId, dateTime, Frequency.D)
            if(business != null){
                business.amount = it.amount
                business.applyCount = it.applyCount
                business.approvalCount = it.approvalCount
                business.datetime = tenantDateTime.now().toDate()
                loanApplicationStatisticsService.save(business)
            }else{
                loanApplicationStatisticsService.saveLoanApplicationStatistics(
                    DTOLoanApplicationStatistics(
                        channel = it.channel,
                        productId = it.productId,
                        productName = it.productName,
                        amount = it.amount,
                        applyCount = it.applyCount,
                        approvalCount = it.approvalCount,
                        frequency = Frequency.D
                    )
                )
            }
        }
    }

    private fun checkLoanApplicationStatisticsExist(
        channel: String,
        productId: Long,
        dateTime: DateTime,
        frequency: Frequency
    ): LoanApplicationStatistics? {
        return loanApplicationStatisticsService.findByDate(
            DTOLoanApplicationStatisticsFindParams(
                channel = channel,
                productId = productId,
                dateTime = dateTime,
                frequency = frequency
            )
        )
    }

}