package cn.sunline.saas.channel.statistics.services

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.channel.statistics.modules.db.LoanApplicationStatistics
import cn.sunline.saas.channel.statistics.modules.dto.DTOLoanApplicationStatistics
import cn.sunline.saas.channel.statistics.modules.dto.DTOLoanApplicationStatisticsFindParams
import cn.sunline.saas.channel.statistics.repositories.LoanApplicationStatisticsRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class LoanApplicationStatisticsService(
    private val loanApplicationStatisticsRepository: LoanApplicationStatisticsRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime
) : BaseMultiTenantRepoService<LoanApplicationStatistics, Long>(loanApplicationStatisticsRepository) {


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
        productId: Long?,
        frequency: Frequency?,
        pageable: Pageable
    ): Page<LoanApplicationStatistics> {
        return getPaged({ root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            tenantId?.let { predicates.add(criteriaBuilder.equal(root.get<Long>("tenantId"), it)) }
            channelCode?.let { predicates.add(criteriaBuilder.equal(root.get<String>("channelCode"), it)) }
            channelName?.let { predicates.add(criteriaBuilder.equal(root.get<String>("channelName"), it)) }
            productId?.let { predicates.add(criteriaBuilder.equal(root.get<Long>("productId"), it)) }
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

    fun findByDate(dtoLoanApplicationStatisticsFindParams: DTOLoanApplicationStatisticsFindParams): LoanApplicationStatistics? {
        val dateTime = dtoLoanApplicationStatisticsFindParams.dateTime
        return getOneWithTenant { root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(
                criteriaBuilder.equal(
                    root.get<String>("channelCode"),
                    dtoLoanApplicationStatisticsFindParams.channelCode
                )
            )
            predicates.add(
                criteriaBuilder.equal(
                    root.get<Long>("productId"),
                    dtoLoanApplicationStatisticsFindParams.productId
                )
            )
            predicates.add(criteriaBuilder.equal(root.get<Long>("year"), dateTime.year.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("month"), dateTime.monthOfYear.toLong()))
            predicates.add(criteriaBuilder.equal(root.get<Long>("day"), dateTime.dayOfMonth.toLong()))
            predicates.add(
                criteriaBuilder.equal(
                    root.get<Frequency>("frequency"),
                    dtoLoanApplicationStatisticsFindParams.frequency
                )
            )

            query.orderBy(criteriaBuilder.desc(root.get<Date>("datetime")))

            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }


    fun saveLoanApplicationStatistics(dtoLoanApplicationStatistics: DTOLoanApplicationStatistics): LoanApplicationStatistics {
        val nowDateTime = tenantDateTime.now()
        return save(
            LoanApplicationStatistics(
                id = sequence.nextId(),
                channelCode = dtoLoanApplicationStatistics.channelCode,
                channelName = dtoLoanApplicationStatistics.channelName,
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
                channelCode = dtoLoanApplicationStatistics.channelCode,
                channelName = dtoLoanApplicationStatistics.channelName,
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