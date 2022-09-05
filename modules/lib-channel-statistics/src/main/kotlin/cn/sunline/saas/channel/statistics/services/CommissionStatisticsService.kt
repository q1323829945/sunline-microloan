package cn.sunline.saas.channel.statistics.services

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.channel.statistics.modules.db.CommissionStatistics
import cn.sunline.saas.channel.statistics.modules.dto.DTOCommissionStatistics
import cn.sunline.saas.channel.statistics.modules.dto.DTOCommissionStatisticsFindParams
import cn.sunline.saas.channel.statistics.repositories.CommissionStatisticsRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
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
                    predicates.add(
                        criteriaBuilder.between(
                            root.get("month"),
                            startMonth,
                            endMonth
                        )
                    )
                }
            }
            startDay?.let {
                endDay?.let {
                    predicates.add(
                        criteriaBuilder.between(
                            root.get("day"),
                            startDay,
                            endDay
                        )
                    )
                }
            }

            query.orderBy(criteriaBuilder.desc(root.get<Date>("datetime")))
            criteriaBuilder.and(*(predicates.toTypedArray()))

        }, pageable)
    }

    fun findByDate(dtoCommissionStatisticsFindParams: DTOCommissionStatisticsFindParams): CommissionStatistics? {
        val dateTime = dtoCommissionStatisticsFindParams.dateTime
        return getOneWithTenant { root, query, criteriaBuilder ->
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
        }
    }


    fun saveCommissionStatistics(dtoCommissionStatistics: DTOCommissionStatistics): CommissionStatistics {
        val nowDateTime = tenantDateTime.now()
        return save(
            CommissionStatistics(
                id = sequence.nextId(),
                commissionFeatureId = dtoCommissionStatistics.commissionFeatureId,
                channelCode = dtoCommissionStatistics.channelCode,
                channelName = dtoCommissionStatistics.channelName,
                statisticsAmount = dtoCommissionStatistics.statisticsAmount,
                amount = dtoCommissionStatistics.amount,
                frequency = dtoCommissionStatistics.frequency,
                year = nowDateTime.year.toLong(),
                month = nowDateTime.monthOfYear.toLong(),
                day = nowDateTime.dayOfMonth.toLong(),
                datetime = nowDateTime.toDate()
            )
        )
    }

    fun saveCommissionStatisticsBySchedule(dtoCommissionStatistics: DTOCommissionStatistics): CommissionStatistics {
        val lastDateTime = tenantDateTime.now().plusDays(-1)
        return save(
            CommissionStatistics(
                id = sequence.nextId(),
                commissionFeatureId = dtoCommissionStatistics.commissionFeatureId,
                channelCode = dtoCommissionStatistics.channelCode,
                channelName = dtoCommissionStatistics.channelName,
                statisticsAmount = dtoCommissionStatistics.statisticsAmount,
                amount = dtoCommissionStatistics.amount,
                frequency = dtoCommissionStatistics.frequency,
                year = lastDateTime.year.toLong(),
                month = lastDateTime.monthOfYear.toLong(),
                day = lastDateTime.dayOfMonth.toLong(),
                datetime = tenantDateTime.now().toDate()
            )
        )
    }
}