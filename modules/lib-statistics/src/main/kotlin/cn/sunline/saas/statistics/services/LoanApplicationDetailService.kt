package cn.sunline.saas.statistics.services

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.statistics.modules.db.LoanApplicationDetail
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationCount
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationDetail
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationDetailQueryParams
import cn.sunline.saas.statistics.repositories.LoanApplicationDetailRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class LoanApplicationDetailService(
    private val loanApplicationDetailRepository: LoanApplicationDetailRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime
) : BaseMultiTenantRepoService<LoanApplicationDetail, Long>(loanApplicationDetailRepository) {

//    data class GroupKey(val channelId: Long,val productId: Long)
//
//    fun LoanApplicationDetail.toKey() = GroupKey(channelId,productId)

    fun saveApplicationDetail(dtoLoanApplicationDetail: DTOLoanApplicationDetail) {
        save(
            LoanApplicationDetail(
                id = sequence.nextId(),
                channel = dtoLoanApplicationDetail.channel,
                productId = dtoLoanApplicationDetail.productId,
                productName = dtoLoanApplicationDetail.productName,
                applicationId = dtoLoanApplicationDetail.applicationId,
                amount = dtoLoanApplicationDetail.amount,
                datetime = tenantDateTime.now().toDate(),
                currency = dtoLoanApplicationDetail.currency,
                status = dtoLoanApplicationDetail.status
            )
        )
    }

    fun getGroupByStatusCount(dtoLoanApplicationDetailQueryParams: DTOLoanApplicationDetailQueryParams): List<DTOLoanApplicationCount> {
        val statusList = getAllByParams(dtoLoanApplicationDetailQueryParams)
        val groupBy = statusList.content.groupBy { it.channel }

        val dtoLoanApplicationCount = mutableListOf<DTOLoanApplicationCount>()
        groupBy.forEach { channelMap ->
            val productGroupBy = channelMap.value.groupBy { it.productId }
            productGroupBy.forEach { productMap ->
                dtoLoanApplicationCount += DTOLoanApplicationCount(
                    channel = channelMap.key,
                    amount = productMap.value.sumOf { it.amount },
                    applyCount = productMap.value.count().toLong(),
                    approvalCount = productMap.value.count { ApplyStatus.APPROVALED == it.status }.toLong(),
                    productId = productMap.key,
                    productName = productMap.value.first().productName
                )
            }
        }
        return dtoLoanApplicationCount
    }


    private fun getAllByParams(dtoLoanApplicationDetailQueryParams: DTOLoanApplicationDetailQueryParams): Page<LoanApplicationDetail> {
        return getPageWithTenant({ root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(
                criteriaBuilder.between(
                    root.get("datetime"),
                    tenantDateTime.toTenantDateTime(dtoLoanApplicationDetailQueryParams.startDateTime).toDate(),
                    tenantDateTime.toTenantDateTime(dtoLoanApplicationDetailQueryParams.endDateTime).toDate()
                )
            )
            query.orderBy(criteriaBuilder.desc(root.get<Date>("datetime")))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
    }

    fun getByApplicationId(applicationId: Long): LoanApplicationDetail? {
        return getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("applicationId"), applicationId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }
}