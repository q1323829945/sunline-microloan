package cn.sunline.saas.satatistics.service

import cn.sunline.saas.channel.party.organisation.service.OrganisationService
import cn.sunline.saas.channel.statistics.services.LoanApplicationStatisticsService
import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.constant.YesOrNo
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.satatistics.service.dto.ChannelStatisticsAmount
import cn.sunline.saas.satatistics.service.dto.ChannelStatisticsCount
import cn.sunline.saas.satatistics.service.dto.DTOChannelStatistics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class ChannelStatisticsManagerService(
    private val tenantDateTime: TenantDateTime
) {
    @Autowired
    private lateinit var loanApplicationStatisticsService: LoanApplicationStatisticsService

    @Autowired
    private lateinit var organisationService: OrganisationService

    fun getChannelGraphics(
        startDate: String?,
        endDate: String?,
        tenantId: Long?,
        channelCode: String?,
        channelName: String?,
        productId: String?,
        applyStatus: ApplyStatus?,
        frequency: Frequency?,
        pageable: Pageable
    ): MutableList<DTOChannelStatistics> {
        val tenantIdData = tenantId ?: ContextUtil.getTenant().toLong()
        val frequencyData = frequency ?: Frequency.D
        val endDateTime =
            if (endDate.isNullOrEmpty()) tenantDateTime.now() else tenantDateTime.toTenantDateTime(endDate)
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

        val channelList = organisationService.getOrganisationListByEnable(YesOrNo.Y,channelCode)
        val list = mutableListOf<DTOChannelStatistics>()
        channelList.forEach { root ->
            val paged = loanApplicationStatisticsService.getPaged(
                startDateTime.year.toLong(),
                endDateTime.year.toLong(),
                startDateTime.monthOfYear.toLong(),
                endDateTime.monthOfYear.toLong(),
                startDateTime.dayOfMonth.toLong(),
                endDateTime.dayOfMonth.toLong(),
                tenantIdData,
                if (channelName.isNullOrEmpty()) root.channelCode else channelCode,
                if (channelName.isNullOrEmpty()) null else channelName,
                if (productId.isNullOrEmpty()) null else productId.toLong(),
                frequencyData,
                Pageable.unpaged()
            ).content
            list += DTOChannelStatistics(
                channelCode = root.channelCode,
                channelName = root.channelName, // TODO IS NULL
                applyStatus = applyStatus,
                count = when (applyStatus) {
                    ApplyStatus.APPROVALED -> paged.sumOf { it.approvalCount }
                    ApplyStatus.RECORD -> paged.sumOf { it.applyCount }
                    else -> 0L
                },
                amount = when (applyStatus) {
                    ApplyStatus.APPROVALED -> paged.sumOf { it.approvalAmount }
                    ApplyStatus.RECORD -> paged.sumOf { it.applyAmount }
                    else -> BigDecimal.ZERO
                },
            )
        }
        return list
    }
}