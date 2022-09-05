package cn.sunline.saas.channel.statistics.services

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.channel.statistics.modules.db.LoanApplicationDetail
import cn.sunline.saas.channel.statistics.modules.dto.DTOLoanApplicationCount
import cn.sunline.saas.channel.statistics.modules.dto.DTOLoanApplicationDetail
import cn.sunline.saas.channel.statistics.modules.dto.DTOLoanApplicationDetailQueryParams
import cn.sunline.saas.channel.statistics.repositories.LoanApplicationDetailRepository
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

    fun saveApplicationDetail(dtoLoanApplicationDetail: DTOLoanApplicationDetail) {
        save(
            LoanApplicationDetail(
                id = sequence.nextId(),
                channelCode = dtoLoanApplicationDetail.channelCode,
                channelName = dtoLoanApplicationDetail.channelName,
                productId = dtoLoanApplicationDetail.productId,
                productName = dtoLoanApplicationDetail.productName,
                applicationId = dtoLoanApplicationDetail.applicationId,
                amount = dtoLoanApplicationDetail.amount,
                datetime = tenantDateTime.now().toDate(),
                status = dtoLoanApplicationDetail.status
            )
        )
    }

    fun getGroupByStatusCount(dtoLoanApplicationDetailQueryParams: DTOLoanApplicationDetailQueryParams): List<DTOLoanApplicationCount> {
        val statusList = getAllByParams(dtoLoanApplicationDetailQueryParams)
        val dtoLoanApplicationCounts = mutableListOf<DTOLoanApplicationCount>()
        statusList.content.groupBy { it.channelCode }
            .forEach { channelCodeMap ->
                channelCodeMap.value.groupBy { it.productId }.forEach { productIdMap ->
                    dtoLoanApplicationCounts += DTOLoanApplicationCount(
                        channelCode = channelCodeMap.key,
                        channelName = productIdMap.value.first().channelName,
                        amount = productIdMap.value.sumOf { it.amount },
                        applyCount = productIdMap.value.count().toLong(),
                        approvalCount = productIdMap.value.count { ApplyStatus.APPROVALED == it.status }.toLong(),
                        productId = productIdMap.key,
                        productName = productIdMap.value.first().productName
                    )
                }
            }
        return dtoLoanApplicationCounts
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

    fun getByApplicationId(applicationId: String): LoanApplicationDetail? {
        return getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("applicationId"), applicationId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }
}