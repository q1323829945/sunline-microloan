package cn.sunline.saas.statistics.services

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.statistics.modules.db.CommissionStatistics
import cn.sunline.saas.statistics.modules.db.LoanApplicationStatistics
import cn.sunline.saas.statistics.modules.dto.DTOCommissionStatistics
import cn.sunline.saas.statistics.modules.dto.DTOCommissionStatisticsFindParams
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationStatistics
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationStatisticsFindParams
import cn.sunline.saas.statistics.repositories.CommissionStatisticsRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class CommissionStatisticsService (
    private val commissionStatisticsRepository: CommissionStatisticsRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime
):BaseMultiTenantRepoService<CommissionStatistics, Long>(commissionStatisticsRepository) {


    fun getPaged(year: Long?, month: Long?, day: Long?, tenantId: Long?, pageable: Pageable): Page<CommissionStatistics> {
        return getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            year?.let{ predicates.add(criteriaBuilder.equal(root.get<Long>("year"), it)) }
            month?.let{ predicates.add(criteriaBuilder.equal(root.get<Long>("month"), it)) }
            day?.let{ predicates.add(criteriaBuilder.equal(root.get<Long>("day"), it)) }
            tenantId?.let{ predicates.add(criteriaBuilder.equal(root.get<Long>("tenantId"), it)) }
            predicates.add(criteriaBuilder.equal(root.get<Frequency>("frequency"), Frequency.D))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable)
    }

    fun findByDate(dtoCommissionStatisticsFindParams: DTOCommissionStatisticsFindParams): CommissionStatistics?{
        val dateTime = dtoCommissionStatisticsFindParams.dateTime
        return getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("channel"),dtoCommissionStatisticsFindParams.channel))
            predicates.add(criteriaBuilder.equal(root.get<Long>("year"),dateTime.year.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("month"),dateTime.monthOfYear.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("day"),dateTime.dayOfMonth.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Frequency>("frequency"),dtoCommissionStatisticsFindParams.frequency))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }


    fun saveCommissionStatistics(dtoCommissionStatistics: DTOCommissionStatistics): CommissionStatistics {
        val nowDateTime = tenantDateTime.now()
        return save(
            CommissionStatistics(
                id = sequence.nextId(),
                commissionFeatureId = dtoCommissionStatistics.commissionFeatureId,
                channel = dtoCommissionStatistics.channel,
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
                channel = dtoCommissionStatistics.channel,
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