package cn.sunline.saas.statistics.services

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.statistics.modules.db.LoanApplicationStatistics
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationStatistics
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationStatisticsFindParams
import cn.sunline.saas.statistics.repositories.LoanApplicationStatisticsRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class LoanApplicationStatisticsService (
    private val loanApplicationStatisticsRepository: LoanApplicationStatisticsRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime
):BaseMultiTenantRepoService<LoanApplicationStatistics, Long>(loanApplicationStatisticsRepository) {


    fun getPaged(year: Long?, month: Long?, day: Long?, tenantId: Long?, pageable: Pageable): Page<LoanApplicationStatistics> {
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

    fun findByDate(dtoLoanApplicationStatisticsFindParams: DTOLoanApplicationStatisticsFindParams): LoanApplicationStatistics?{
        val dateTime = dtoLoanApplicationStatisticsFindParams.dateTime
        return getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("channel"),dtoLoanApplicationStatisticsFindParams.channel))
            predicates.add(criteriaBuilder.equal(root.get<Long>("productId"),dtoLoanApplicationStatisticsFindParams.productId))
            predicates.add(criteriaBuilder.equal(root.get<Long>("year"),dateTime.year.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("month"),dateTime.monthOfYear.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("day"),dateTime.dayOfMonth.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Frequency>("frequency"),dtoLoanApplicationStatisticsFindParams.frequency))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }


    fun saveLoanApplicationStatistics(dtoLoanApplicationStatistics: DTOLoanApplicationStatistics): LoanApplicationStatistics {
        val nowDateTime = tenantDateTime.now()
        return save(
            LoanApplicationStatistics(
                id = sequence.nextId(),
                channel = dtoLoanApplicationStatistics.channel,
                productId = dtoLoanApplicationStatistics.productId,
                productName = dtoLoanApplicationStatistics.productName,
                amount = dtoLoanApplicationStatistics.amount,
                applyCount = dtoLoanApplicationStatistics.applyCount,
                approvalCount = dtoLoanApplicationStatistics.approvalCount,
                frequency = dtoLoanApplicationStatistics.frequency,
                year = nowDateTime.year.toLong(),
                month = nowDateTime.monthOfYear.toLong(),
                day = nowDateTime.dayOfMonth.toLong(),
                datetime = nowDateTime.toDate()
            )
        )
    }

    fun saveLoanApplicationStatisticBySchedule(dtoLoanApplicationStatistics: DTOLoanApplicationStatistics): LoanApplicationStatistics {
        val lastDateTime = tenantDateTime.now().plusDays(-1)
        return save(
            LoanApplicationStatistics(
                id = sequence.nextId(),
                channel = dtoLoanApplicationStatistics.channel,
                productId = dtoLoanApplicationStatistics.productId,
                productName = dtoLoanApplicationStatistics.productName,
                amount = dtoLoanApplicationStatistics.amount,
                applyCount = dtoLoanApplicationStatistics.applyCount,
                approvalCount = dtoLoanApplicationStatistics.approvalCount,
                frequency = dtoLoanApplicationStatistics.frequency,
                year = lastDateTime.year.toLong(),
                month = lastDateTime.monthOfYear.toLong(),
                day = lastDateTime.dayOfMonth.toLong(),
                datetime = tenantDateTime.now().toDate()
            )
        )
    }
}