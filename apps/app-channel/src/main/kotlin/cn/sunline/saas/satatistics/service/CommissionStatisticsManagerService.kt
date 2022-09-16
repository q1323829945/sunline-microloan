package cn.sunline.saas.satatistics.service

import cn.sunline.saas.channel.agreement.service.ChannelAgreementService
import cn.sunline.saas.channel.arrangement.service.ChannelArrangementService
import cn.sunline.saas.channel.party.organisation.service.ChannelCastService
import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.statistics.modules.db.CommissionStatistics
import cn.sunline.saas.channel.statistics.modules.dto.*
import cn.sunline.saas.channel.statistics.services.CommissionDetailService
import cn.sunline.saas.channel.statistics.services.CommissionStatisticsService
import cn.sunline.saas.loan.exception.LoanApplyNotFoundException
import cn.sunline.saas.satatistics.service.dto.DTOCommissionChartsAmount
import cn.sunline.saas.satatistics.service.dto.DTOCommissionStatisticsCharts
import cn.sunline.saas.satatistics.service.dto.DTOCommissionStatisticsCount
import cn.sunline.saas.satatistics.service.dto.DTOCommissionStatisticsPageCount
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import javax.persistence.criteria.Predicate


@Service
class CommissionStatisticsManagerService(
    private val tenantDateTime: TenantDateTime
) {

    @Autowired
    private lateinit var commissionStatisticsService: CommissionStatisticsService

    @Autowired
    private lateinit var commissionDetailService: CommissionDetailService

    fun getPaged(
        startDate: String?,
        endDate: String?,
        tenantId: Long?,
        channelCode: String?,
        channelName: String?,
        frequency: Frequency?,
        pageable: Pageable
    ): Page<DTOCommissionStatisticsPageCount> {
        val tenantIdData = tenantId ?: ContextUtil.getTenant().toLong()
        val frequencyData = frequency ?: Frequency.D
        val endDateTime = if (endDate.isNullOrEmpty()) tenantDateTime.now() else tenantDateTime.toTenantDateTime(endDate)
        val startDateTime = if (startDate.isNullOrEmpty()) {
            when (frequencyData) {
                Frequency.Y -> {
                    endDateTime.plusYears(-7)
                }
                Frequency.M -> {
                    endDateTime.plusMonths(-7)
                }
                else -> {
                    endDateTime.plusDays(-7)
                }
            }
        } else {
            tenantDateTime.toTenantDateTime(startDate)
        }

        return commissionStatisticsService.getPaged(
            startDateTime.year.toLong(),
            endDateTime.year.toLong(),
            startDateTime.monthOfYear.toLong(),
            endDateTime.monthOfYear.toLong(),
            startDateTime.dayOfMonth.toLong(),
            endDateTime.dayOfMonth.toLong(),
            tenantIdData,
            if (channelCode.isNullOrEmpty()) null else channelCode,
            if (channelName.isNullOrEmpty()) null else channelName,
            frequencyData,
            pageable
        ).map {
            DTOCommissionStatisticsPageCount(
                channelCode = it.channelCode,
                channelName = it.channelName,
                statisticsAmount = it.statisticsAmount,
                amount = it.amount,
                commissionFeatureId = it.commissionFeatureId,
                dateTime = tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(it.datetime))
            )
        }
    }

    fun addCommissionDetail(dtoCommissionDetail: DTOCommissionDetail) {
        commissionDetailService.getByApplicationId(dtoCommissionDetail.applicationId) ?: run {
            commissionDetailService.saveCommissionDetail(dtoCommissionDetail)
        }
    }

    fun addCommissionStatistics() {
        val nowDate = tenantDateTime.now()
//        if (nowDate.hourOfDay == 0) {
            //根据租户时区统计数据
            //每日统计
            saveDay(nowDate)
            //每月统计
            if (nowDate.dayOfMonth == 1) {
                saveMonth(nowDate)

                //每年统计
                if (nowDate.monthOfYear == 1) {
                    saveYear(nowDate)
                }
            }
//        }
    }

    private fun saveYear(dateTime: DateTime) {
        val startDate = tenantDateTime.toTenantDateTime(dateTime.toLocalDate().toDate())
        val endDate = startDate.plusYears(1)
        addCommissionStatistics(dateTime, startDate.toDate(), endDate.toDate(), Frequency.Y)
    }

    private fun saveMonth(dateTime: DateTime) {
        val startDate = tenantDateTime.toTenantDateTime(dateTime.toLocalDate().toDate())
        val endDate = startDate.plusMonths(1)
        addCommissionStatistics(dateTime, startDate.toDate(), endDate.toDate(), Frequency.M)
    }

    private fun saveDay(dateTime: DateTime) {
        val startDate = tenantDateTime.toTenantDateTime(dateTime.toLocalDate().toDate())
        val endDate = startDate.plusDays(1)
        addCommissionStatistics(dateTime, startDate.toDate(), endDate.toDate(), Frequency.D)
    }

    private fun addCommissionStatistics(dateTime: DateTime, startDate: Date, endDate: Date, frequency: Frequency) {
        val commission = commissionDetailService.getGroupByStatusCount(
            DTOCommissionDetailQueryParams(
                startDate,
                endDate
            )
        )
        commission.forEach { it ->
            val business = checkCommissionStatisticsExist(it.channelCode, it.channelName,dateTime, frequency)
            if (business != null) {
                business.amount = it.amount
                business.statisticsAmount = it.statisticsAmount
                business.datetime = tenantDateTime.now().toDate()
                commissionStatisticsService.save(business)
            } else {
                commissionStatisticsService.saveCommissionStatistics(
                    DTOCommissionStatistics(
                        channelCode = it.channelCode,
                        channelName = it.channelName,
                        commissionFeatureId = 0,
                        statisticsAmount = it.statisticsAmount,
                        amount = it.amount,
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

    fun getChartsPaged(
        startDate: String?,
        endDate: String?,
        tenantId: Long?,
        channelCode: String?,
        channelName: String?,
        frequency: Frequency?
    ): DTOCommissionStatisticsCharts {
        val tenantIdData = tenantId ?: ContextUtil.getTenant().toLong()
        val frequencyData = frequency ?: Frequency.D
        val endDateTime = if (endDate.isNullOrEmpty()) tenantDateTime.now() else tenantDateTime.toTenantDateTime(endDate)
        val startDateTime = if (startDate.isNullOrEmpty()) {
            when (frequencyData) {
                Frequency.Y -> {
                    endDateTime.plusYears(-6)
                }
                Frequency.M -> {
                    endDateTime.plusMonths(-6)
                }
                else -> {
                    endDateTime.plusDays(-6)
                }
            }
        } else {
            tenantDateTime.toTenantDateTime(startDate)
        }

        val page = commissionStatisticsService.getPaged(
            startDateTime.year.toLong(),
            endDateTime.year.toLong(),
            if (Frequency.Y != frequency) startDateTime.monthOfYear.toLong() else null,
            if (Frequency.Y != frequency) endDateTime.monthOfYear.toLong() else null,
            if (Frequency.D == frequency) startDateTime.dayOfMonth.toLong() else null,
            if (Frequency.D == frequency) endDateTime.dayOfMonth.toLong() else null,
            tenantIdData,
            if (channelCode.isNullOrEmpty()) null else channelCode,
            if (channelName.isNullOrEmpty()) null else channelName,
            frequencyData,
            Pageable.unpaged()
        )
        val commissionChartsAmount = ArrayList<DTOCommissionChartsAmount>()
        val groupBy = page.sortedBy { it.datetime }.groupBy {
            when (frequency) {
                Frequency.D -> it.year.toString() + String.format("%02d", it.month) + String.format("%02d", it.day)
                Frequency.M -> it.year.toString() + String.format("%02d", it.month)
                Frequency.Y -> it.year.toString()
                else -> ""
            }
        }
        groupBy.forEach { it ->
            commissionChartsAmount += DTOCommissionChartsAmount(
                channelCode = if (channelCode == null && channelName == null) null else it.value.first().channelCode,
                channelName =  if (channelCode == null && channelName == null) null else it.value.first().channelName,
                amount = it.value.sumOf { it.amount },
                dateTime = it.key
            )
        }
        return DTOCommissionStatisticsCharts(
            channelCode = channelCode,
            channelName = channelName,
            commissionAmount = reChartsPaged(
                commissionChartsAmount,
                startDateTime,
                endDateTime,
                channelCode,
                channelName,
                frequencyData
            )
        )
    }

    private fun reChartsPaged(
        content: List<DTOCommissionChartsAmount>,
        start: DateTime,
        end: DateTime,
        channelCode: String?,
        channelName: String?,
        frequency: Frequency
    ): List<DTOCommissionChartsAmount> {
        val list = ArrayList<DTOCommissionChartsAmount>()
        val dateTimes = getDateRange(start, end, frequency)
        dateTimes.forEach { dateTimeString ->
            val first = content.firstOrNull { it.dateTime == dateTimeString }
            list += DTOCommissionChartsAmount(
                channelCode = first?.channelCode ?: channelCode,
                channelName = first?.channelName ?: channelName,
                amount = first?.amount ?: BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                dateTime = dateTimeString
            )
        }
        return list
    }


    private fun getDateRange(start: DateTime, end: DateTime, frequency: Frequency): List<String> {
        val list = ArrayList<String>()
        var flag = true
        var index = 0
        while (flag) {
            when (frequency) {
                Frequency.Y -> {
                    val endDateTime = start.plusYears(index)
                    list.add(endDateTime.year.toString())
                    if (endDateTime.year == end.year) {
                        flag = false
                    }
                }
                Frequency.M -> {
                    val endDateTime = start.plusMonths(index)
                    list.add(tenantDateTime.getYearMonth(endDateTime))
                    if (endDateTime.year == end.year &&
                        endDateTime.monthOfYear == end.monthOfYear
                    ) {
                        flag = false
                    }
                }
                Frequency.D -> {
                    val endDateTime = start.plusDays(index)
                    list.add(tenantDateTime.getYearMonthDay(endDateTime))
                    if (endDateTime.year == end.year &&
                        endDateTime.monthOfYear == end.monthOfYear &&
                        endDateTime.dayOfMonth == end.dayOfMonth
                    ) {
                        flag = false
                    }
                }
                else -> {
                    flag = false
                }
            }
            index++
        }
        return list
    }

    fun getStatisticsByDate(year:Long,month:Long,day:Long,tenantId:Long): List<DTOCommissionStatisticsCount> {
        val page = commissionStatisticsService.getPaged({
                root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("year"),year))
            predicates.add(criteriaBuilder.equal(root.get<Long>("month"),month))
            predicates.add(criteriaBuilder.equal(root.get<Long>("day"),day))
            predicates.add(criteriaBuilder.equal(root.get<Frequency>("frequency"), Frequency.D))
            predicates.add(criteriaBuilder.equal(root.get<Long>("tenantId"), tenantId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())


        return page.content.groupBy { it.channelCode }.map { groupBy ->
            DTOCommissionStatisticsCount(
                channelCode = groupBy.key,
                channelName = groupBy.value.first().channelName,
                amount = groupBy.value.sumOf { it.amount },
                statisticsAmount = groupBy.value.sumOf { it.statisticsAmount },
                tenantId = tenantId
            )
        }
    }
}