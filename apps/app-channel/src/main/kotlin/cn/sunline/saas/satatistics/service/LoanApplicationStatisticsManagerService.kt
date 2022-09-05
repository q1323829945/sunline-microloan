package cn.sunline.saas.satatistics.service

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.satatistics.service.dto.*
import cn.sunline.saas.channel.statistics.modules.db.LoanApplicationStatistics
import cn.sunline.saas.channel.statistics.modules.dto.*
import cn.sunline.saas.channel.statistics.services.LoanApplicationDetailService
import cn.sunline.saas.channel.statistics.services.LoanApplicationStatisticsService
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
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
        startDate: String?,
        endDate: String?,
        tenantId: Long?,
        channelCode: String?,
        channelName: String?,
        productId: String?,
        frequency: Frequency?,
        pageable: Pageable
    ): Page<DTOLoanApplicationStatisticsCount> {
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

        return loanApplicationStatisticsService.getPaged(
            startDateTime.year.toLong(),
            endDateTime.year.toLong(),
            startDateTime.monthOfYear.toLong(),
            endDateTime.monthOfYear.toLong(),
            startDateTime.dayOfMonth.toLong(),
            endDateTime.dayOfMonth.toLong(),
            tenantIdData,
            if (channelCode.isNullOrEmpty()) null else channelCode,
            if (channelName.isNullOrEmpty()) null else channelName,
            if (productId.isNullOrEmpty()) null else productId.toLong(),
            frequencyData,
            pageable
        ).map {
            DTOLoanApplicationStatisticsCount(
                dateTime = tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(it.datetime)),
                channelCode = it.channelCode,
                channelName = it.channelName,
                productId = it.productId.toString(),
                productName = it.productName,
                amount = it.amount,
                applyCount = it.applyCount,
                approvalCount = it.approvalCount
            )
        }
    }

    fun addLoanApplicationDetail(dtoLoanApplicationDetail: DTOLoanApplicationDetail) {
        loanApplicationDetailService.getByApplicationId(dtoLoanApplicationDetail.applicationId) ?: run {
            loanApplicationDetailService.saveApplicationDetail(dtoLoanApplicationDetail)
        }
    }

    fun addLoanApplicationStatistics() {
        val nowDate = tenantDateTime.now()
        if (nowDate.hourOfDay == 0) {
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
        }
    }

    private fun saveYear(dateTime: DateTime) {
        val startDate = tenantDateTime.toTenantDateTime(dateTime.toLocalDate().toDate())
        val endDate = startDate.plusYears(1)
        addLoanApplicationStatistics(dateTime, startDate.toDate(), endDate.toDate(), Frequency.Y)
    }

    private fun saveMonth(dateTime: DateTime) {
        val startDate = tenantDateTime.toTenantDateTime(dateTime.toLocalDate().toDate())
        val endDate = startDate.plusMonths(1)
        addLoanApplicationStatistics(dateTime, startDate.toDate(), endDate.toDate(), Frequency.M)
    }

    private fun saveDay(dateTime: DateTime) {
        val startDate = tenantDateTime.toTenantDateTime(dateTime.toLocalDate().toDate())
        val endDate = startDate.plusDays(1)
        addLoanApplicationStatistics(dateTime, startDate.toDate(), endDate.toDate(), Frequency.D)
    }

    fun addLoanApplicationStatistics(dateTime: DateTime, startDate: Date, endDate: Date, frequency: Frequency) {
        val loanApplication =
            loanApplicationDetailService.getGroupByStatusCount(
                DTOLoanApplicationDetailQueryParams(
                    startDate,
                    endDate
                )
            )
        loanApplication.forEach {
            val business = checkLoanApplicationStatisticsExist(it.channelCode, it.productId, dateTime, frequency)
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

    private fun checkLoanApplicationStatisticsExist(
        channelCode: String,
        productId: Long,
        dateTime: DateTime,
        frequency: Frequency
    ): LoanApplicationStatistics? {
        return loanApplicationStatisticsService.findByDate(
            DTOLoanApplicationStatisticsFindParams(
                channelCode = channelCode,
                productId = productId,
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
        productId: String?,
        frequency: Frequency?
    ): DTOLoanApplicationStatisticsCharts {
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

        val page = loanApplicationStatisticsService.getPaged(
            startDateTime.year.toLong(),
            endDateTime.year.toLong(),
            startDateTime.monthOfYear.toLong(),
            endDateTime.monthOfYear.toLong(),
            startDateTime.dayOfMonth.toLong(),
            endDateTime.dayOfMonth.toLong(),
            tenantIdData,
            if (channelCode.isNullOrEmpty()) null else channelCode,
            if (channelName.isNullOrEmpty()) null else channelName,
            if (productId.isNullOrEmpty()) null else productId.toLong(),
            frequencyData,
            Pageable.unpaged()
        )
        val applyChartsCount = ArrayList<DTOLoanApplicationChartsCount>()
        val approvalChartsCount = ArrayList<DTOLoanApplicationChartsApprovalCount>()
        val applyChartsAmount = ArrayList<DTOLoanApplicationChartsAmount>()
        var productName: String? = null
        val groupBy = page.sortedBy { it.datetime }.groupBy {
            when (frequency) {
                Frequency.D -> it.year.toString() + String.format("%02d", it.month) + String.format("%02d", it.day)
                Frequency.M -> it.year.toString() + String.format("%02d", it.month)
                Frequency.Y -> it.year.toString()
                Frequency.Q -> ""
                else -> ""
            }
        }
        groupBy.forEach { it ->
            productName = if (productId == null) null else it.value.first().productName
            applyChartsCount += DTOLoanApplicationChartsCount(
                channelCode = if (channelCode == null && channelName == null) null else it.value.first().channelCode,
                channelName = if (channelCode == null && channelName == null) null else it.value.first().channelName,
                productId = if (productId == null) null else it.value.first().productId.toString(),
                productName = productName,
                applyCount = it.value.sumOf { it.applyCount },
                dateTime = it.key
            )

            approvalChartsCount += DTOLoanApplicationChartsApprovalCount(
                channelCode = if (channelCode == null && channelName == null) null else it.value.first().channelCode,
                channelName = if (channelCode == null && channelName == null) null else it.value.first().channelName,
                productId = if (productId == null) null else it.value.first().productId.toString(),
                productName = productName,
                approvalCount = it.value.sumOf { it.approvalCount },
                dateTime = it.key
            )

            applyChartsAmount += DTOLoanApplicationChartsAmount(
                channelCode = if (channelCode == null && channelName == null) null else it.value.first().channelCode,
                channelName = if (channelCode == null && channelName == null) null else it.value.first().channelName,
                productId = if (productId == null) null else it.value.first().productId.toString(),
                productName = productName,
                amount = it.value.sumOf { it.amount },
                dateTime = it.key
            )
        }
        return DTOLoanApplicationStatisticsCharts(
            channelCode = channelCode,
            channelName = channelName,
            productId = productId,
            productName = productName,
            applyCount = reApplyCountList(applyChartsCount, startDateTime, endDateTime, frequencyData),
            approvalCount = reApprovalCountList(approvalChartsCount, startDateTime, endDateTime, frequencyData),
            applyAmount = reApplyAmountList(applyChartsAmount, startDateTime, endDateTime, frequencyData)
        )
    }

    private fun reApplyCountList(
        content: List<DTOLoanApplicationChartsCount>,
        start: DateTime,
        end: DateTime,
        frequency: Frequency
    ): List<DTOLoanApplicationChartsCount> {
        val list = ArrayList<DTOLoanApplicationChartsCount>()
        val dateTimes = getDateRange(start, end, frequency)
        dateTimes.forEach { dateTimeString ->
            val first = content.firstOrNull { it.dateTime == dateTimeString }
            list += DTOLoanApplicationChartsCount(
                channelCode = first?.channelCode,
                channelName = first?.channelName,
                productId = first?.productId,
                productName = first?.productName,
                applyCount = first?.applyCount ?: 0L,
                dateTime = dateTimeString
            )
        }
        return list
    }

    private fun reApprovalCountList(
        content: List<DTOLoanApplicationChartsApprovalCount>,
        start: DateTime,
        end: DateTime,
        frequency: Frequency
    ): List<DTOLoanApplicationChartsApprovalCount> {
        val list = ArrayList<DTOLoanApplicationChartsApprovalCount>()
        val dateTimes = getDateRange(start, end, frequency)
        dateTimes.forEach { dateTimeString ->
            val first = content.firstOrNull { it.dateTime == dateTimeString }
            list += DTOLoanApplicationChartsApprovalCount(
                channelCode = first?.channelCode,
                channelName = first?.channelName,
                productId = first?.productId,
                productName = first?.productName,
                approvalCount = first?.approvalCount ?: 0L,
                dateTime = dateTimeString
            )
        }
        return list
    }

    private fun reApplyAmountList(
        content: List<DTOLoanApplicationChartsAmount>,
        start: DateTime,
        end: DateTime,
        frequency: Frequency
    ): List<DTOLoanApplicationChartsAmount> {
        val list = ArrayList<DTOLoanApplicationChartsAmount>()
        val dateTimes = getDateRange(start, end, frequency)
        dateTimes.forEach { dateTimeString ->
            val first = content.firstOrNull { it.dateTime == dateTimeString }
            list += DTOLoanApplicationChartsAmount(
                channelCode = first?.channelCode,
                channelName = first?.channelName,
                productId = first?.productId,
                productName = first?.productName,
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

}