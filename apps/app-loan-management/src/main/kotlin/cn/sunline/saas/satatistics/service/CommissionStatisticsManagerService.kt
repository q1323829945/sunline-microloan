package cn.sunline.saas.satatistics.service

import cn.sunline.saas.consumer_loan.service.dto.DTOFeeItemView
import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.rpc.invoke.impl.PageInvokeImpl
import cn.sunline.saas.satatistics.service.dto.DTOCommissionStatisticsCount
import cn.sunline.saas.satatistics.service.dto.DTOLoanApplicationStatisticsCount
import cn.sunline.saas.statistics.modules.db.CommissionStatistics
import cn.sunline.saas.statistics.modules.db.LoanApplicationStatistics
import cn.sunline.saas.statistics.modules.dto.*
import cn.sunline.saas.statistics.services.CommissionDetailService
import cn.sunline.saas.statistics.services.CommissionStatisticsService
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode


@Service
class CommissionStatisticsManagerService(
    private val tenantDateTime: TenantDateTime
) {

    @Autowired
    private lateinit var commissionStatisticsService: CommissionStatisticsService

    @Autowired
    private lateinit var commissionDetailService: CommissionDetailService

    private val ratio = BigDecimal(0.2).setScale(2,RoundingMode.HALF_UP)

    fun getPaged(
        year: Long?,
        month: Long?,
        day: Long?,
        tenantId: Long?,
        pageable: Pageable
    ): Page<DTOCommissionStatisticsCount> {
        val page = commissionStatisticsService.getPaged(year, month, day, tenantId, pageable)
        val groupByChannel = page.content.groupBy { it.channel }
        val dtoCommissionStatisticsCountList = mutableListOf<DTOCommissionStatisticsCount>()
        groupByChannel.forEach{ channelMap->
            val groupByYear = channelMap.value.groupBy { it.year }
            groupByYear.forEach{ yearMap ->
                // TODO  get CommissionFeature by commissionFeatureId ,get the ratio
                val groupByMonth = yearMap.value.groupBy { it.month }
                groupByMonth.forEach{ monthMap ->
                    dtoCommissionStatisticsCountList += DTOCommissionStatisticsCount(
                        channel = monthMap.value.first().channel,
                        statisticsAmount = monthMap.value.sumOf { it.statisticsAmount },
                        amount = monthMap.value.sumOf { it.amount },
                        ratio = ratio,
                        commissionFeatureId = monthMap.value.first().commissionFeatureId,
                        dateTime =  tenantDateTime.getYearMonth(tenantDateTime.toTenantDateTime(monthMap.value.first().datetime))
                    )
                }
            }
        }
        return PageInvokeImpl<DTOCommissionStatisticsCount>().rePaged(dtoCommissionStatisticsCountList, pageable)
    }

    fun addCommissionDetail(dtoCommissionDetail: DTOCommissionDetail) {
        commissionDetailService.getByApplicationId(dtoCommissionDetail.applicationId) ?: run {
            commissionDetailService.saveCommissionDetail(dtoCommissionDetail)
        }
    }

    fun addCommissionStatistics() {
        val dateTime = tenantDateTime.now()
        val startDate = tenantDateTime.toTenantDateTime(dateTime.toLocalDate().toDate())
        val endDate = startDate.plusDays(1)
        val commission = commissionDetailService.getGroupByStatusCount(
            DTOCommissionDetailQueryParams(
                startDate.toDate(),
                endDate.toDate()
            )
        )
        commission.forEach {
            // TODO  get CommissionFeature by commissionFeatureId ,get the ratio
            val business = checkCommissionStatisticsExist(it.channel, dateTime, Frequency.D)
            if (business != null) {
                business.amount = it.amount.multiply(ratio).setScale(2,RoundingMode.HALF_UP)
                business.statisticsAmount =  it.amount
                business.datetime = tenantDateTime.now().toDate()
                commissionStatisticsService.save(business)
            } else {
                commissionStatisticsService.saveCommissionStatistics(
                    DTOCommissionStatistics(
                        channel = it.channel,
                        commissionFeatureId = 0,
                        statisticsAmount =  it.amount,
                        amount = it.amount.multiply(ratio).setScale(2,RoundingMode.HALF_UP),
                        frequency = Frequency.D
                    )
                )
            }
        }
    }

    private fun checkCommissionStatisticsExist(
        channel: String,
        dateTime: DateTime,
        frequency: Frequency
    ): CommissionStatistics? {
        return commissionStatisticsService.findByDate(
            DTOCommissionStatisticsFindParams(
                channel = channel,
                dateTime = dateTime,
                frequency = frequency
            )
        )
    }
}