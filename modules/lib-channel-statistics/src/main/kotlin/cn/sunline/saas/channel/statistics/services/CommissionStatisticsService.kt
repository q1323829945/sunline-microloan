package cn.sunline.saas.channel.statistics.services

import cn.sunline.saas.channel.statistics.modules.db.CommissionStatistics
import cn.sunline.saas.channel.statistics.modules.dto.DTOCommissionDetail
import cn.sunline.saas.channel.statistics.modules.dto.DTOCommissionStatistics
import cn.sunline.saas.channel.statistics.modules.dto.DTOCommissionStatisticsFindParams
import cn.sunline.saas.channel.statistics.repositories.CommissionStatisticsRepository
import cn.sunline.saas.global.constant.CommissionMethodType
import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class CommissionStatisticsService(
    private val commissionStatisticsRepository: CommissionStatisticsRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime
) : BaseMultiTenantRepoService<CommissionStatistics, Long>(commissionStatisticsRepository) {


    fun getPaged(
        startYear: Long?,
        endYear: Long?,
        startMonth: Long?,
        endMonth: Long?,
        startDay: Long?,
        endDay: Long?,
        tenantId: Long?,
        channelCode: String?,
        channelName: String?,
        frequency: Frequency?,
        pageable: Pageable
    ): Page<CommissionStatistics> {
        return getPaged({ root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            tenantId?.let { predicates.add(criteriaBuilder.equal(root.get<Long>("tenantId"), it)) }
            channelCode?.let { predicates.add(criteriaBuilder.equal(root.get<String>("channelCode"), it)) }
            channelName?.let { predicates.add(criteriaBuilder.equal(root.get<String>("channelName"), it)) }
            frequency?.let { predicates.add(criteriaBuilder.equal(root.get<Frequency>("frequency"), it)) }

            startYear?.let {
                endYear?.let {
                    predicates.add(
                        criteriaBuilder.between(
                            root.get("year"),
                            startYear,
                            endYear
                        )
                    )
                }
            }
            startMonth?.let {
                endMonth?.let {
                    if (startMonth > endMonth) {
                        predicates.add(
                            criteriaBuilder.between(
                                root.get("month"),
                                endMonth + 1,
                                startMonth
                            ).not()
                        )
                    } else {
                        predicates.add(
                            criteriaBuilder.between(
                                root.get("month"),
                                startMonth,
                                endMonth
                            )
                        )
                    }
                }
            }
            startDay?.let {
                endDay?.let {
                    if (startDay > endDay) {
                        predicates.add(
                            criteriaBuilder.between(
                                root.get("day"),
                                endDay + 1,
                                startDay
                            ).not()
                        )
                    } else {
                        predicates.add(
                            criteriaBuilder.between(
                                root.get("day"),
                                startDay,
                                endDay
                            )
                        )
                    }
                }
            }

            query.orderBy(criteriaBuilder.desc(root.get<Date>("datetime")))
            criteriaBuilder.and(*(predicates.toTypedArray()))

        }, pageable)
    }

    fun findByDate(dtoCommissionStatisticsFindParams: DTOCommissionStatisticsFindParams): Page<CommissionStatistics> {
        val dateTime = dtoCommissionStatisticsFindParams.dateTime
        return getPageWithTenant({ root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(
                criteriaBuilder.equal(
                    root.get<String>("channelCode"),
                    dtoCommissionStatisticsFindParams.channelCode
                )
            )
            predicates.add(criteriaBuilder.equal(root.get<Long>("year"), dateTime.year.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("month"), dateTime.monthOfYear.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("day"), dateTime.dayOfMonth.toLong()))
            predicates.add(
                criteriaBuilder.equal(
                    root.get<Frequency>("frequency"),
                    dtoCommissionStatisticsFindParams.frequency
                )
            )

            query.orderBy(criteriaBuilder.desc(root.get<Date>("datetime")))

            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
    }


    fun saveCommissionStatistics(dtoCommissionStatistics: DTOCommissionStatistics): CommissionStatistics {
        val nowDateTime = dtoCommissionStatistics.dateTime ?: tenantDateTime.now()
        return save(
            CommissionStatistics(
                id = sequence.nextId(),
                channelCode = dtoCommissionStatistics.channelCode,
                channelName = dtoCommissionStatistics.channelName,
                statisticsAmount = dtoCommissionStatistics.statisticsAmount,
                commissionAmount = dtoCommissionStatistics.commissionAmount,
                frequency = dtoCommissionStatistics.frequency,
                year = nowDateTime.year.toLong(),
                month = nowDateTime.monthOfYear.toLong(),
                day = nowDateTime.dayOfMonth.toLong(),
                datetime = nowDateTime.toDate(),
                applyStatus = dtoCommissionStatistics.applyStatus,
                created = tenantDateTime.now().toDate()
            )
        )
    }

    fun saveCommissionStatisticsBySchedule(dtoCommissionStatistics: DTOCommissionStatistics): CommissionStatistics {
        val lastDateTime = tenantDateTime.now().plusDays(-1)
        return save(
            CommissionStatistics(
                id = sequence.nextId(),
                channelCode = dtoCommissionStatistics.channelCode,
                channelName = dtoCommissionStatistics.channelName,
                statisticsAmount = dtoCommissionStatistics.statisticsAmount,
                commissionAmount = dtoCommissionStatistics.commissionAmount,
                frequency = dtoCommissionStatistics.frequency,
                year = lastDateTime.year.toLong(),
                month = lastDateTime.monthOfYear.toLong(),
                day = lastDateTime.dayOfMonth.toLong(),
                datetime = tenantDateTime.now().toDate(),
                applyStatus = dtoCommissionStatistics.applyStatus,
                created = tenantDateTime.now().toDate()
            )
        )
    }


//    private fun syncCommissionStatistics(syncData: SyncData) {
//        var commissionAmount = BigDecimal.ZERO
//        var ratio: BigDecimal? = BigDecimal.ZERO
//        val statisticsAmount = syncData.amount ?: BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
//        val rangeValuesMap = channelArrangementService.getRangeValuesByChannelAgreementId(
//            syncData.channelAgreementId,
//            syncData.status,
//            Pageable.unpaged()
//        )
//        val dto = arrayListOf<DTOCommissionDetail>()
//
//        rangeValuesMap.forEach { (t, u) ->
//
//            val details = commissionDetailService.getListByStatus(t)
//
//            commissionAmount = when (u.first().commissionMethodType) {
//
//                CommissionMethodType.COUNT_FIX_AMOUNT -> {
//                    val count = details.size + 1
//                    ratio = null
//                    ChannelCommissionCalculator(u.first().commissionMethodType).calculate(
//                        count.toBigDecimal(),
//                        u
//                    ) ?: BigDecimal.ZERO
//                }
//
//                CommissionMethodType.AMOUNT_RATIO -> {
//                    ratio = ChannelCommissionCalculator(u.first().commissionMethodType).calculate(statisticsAmount, u)
//                        ?: BigDecimal.ZERO
//                    statisticsAmount.multiply(ratio)
//                }
//
//                else -> {
//                    TODO()
//                }
//            }
//            dto += DTOCommissionDetail(
//                channelCode = syncData.channelCode,
//                channelName = syncData.channelName,
//                applicationId = syncData.applicationId,
//                commissionAmount = commissionAmount,
//                applyStatus = syncData.status,
//                currency = CurrencyType.USD,
//                ratio = ratio,
//                statisticsAmount = statisticsAmount,
//                dateTime = tenantDateTime.toTenantDateTime(syncData.created)
//            )
//        }
//
//        commissionStatisticsManagerService.addCommissionDetail(dto)
//
//        commissionStatisticsManagerService.addCommissionStatistics(tenantDateTime.toTenantDateTime(syncData.created))
//    }
}