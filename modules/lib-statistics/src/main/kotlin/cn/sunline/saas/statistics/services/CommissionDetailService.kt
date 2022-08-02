package cn.sunline.saas.statistics.services

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.statistics.modules.db.CommissionDetail
import cn.sunline.saas.statistics.modules.db.LoanApplicationDetail
import cn.sunline.saas.statistics.modules.dto.*
import cn.sunline.saas.statistics.repositories.CommissionDetailRepository
import cn.sunline.saas.statistics.repositories.LoanApplicationDetailRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class CommissionDetailService(
    private val CommissionDetailRepository: CommissionDetailRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime
) : BaseMultiTenantRepoService<CommissionDetail, Long>(CommissionDetailRepository) {


    fun saveCommissionDetail(dtoCommissionDetail: DTOCommissionDetail) {
        save(
            CommissionDetail(
                id = sequence.nextId(),
                channel = dtoCommissionDetail.channel,
                applicationId = dtoCommissionDetail.applicationId,
                amount = dtoCommissionDetail.amount,
                datetime = tenantDateTime.now().toDate(),
                currency = dtoCommissionDetail.currency,
                status = dtoCommissionDetail.status
            )
        )
    }

    fun getGroupByStatusCount(dtoCommissionDetailQueryParams: DTOCommissionDetailQueryParams): List<DTOCommissionCount> {
        val statusList = getAllByParams(dtoCommissionDetailQueryParams)
        return statusList.content.groupBy { it.channel }.map { it ->
            DTOCommissionCount(
                it.key,
                it.value.filter { it.status == ApplyStatus.APPROVALED }.sumOf { it.amount })
        }
    }


    private fun getAllByParams(dtoCommissionDetailQueryParams: DTOCommissionDetailQueryParams): Page<CommissionDetail> {
        return getPageWithTenant({ root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(
                criteriaBuilder.between(
                    root.get("datetime"),
                    tenantDateTime.toTenantDateTime(dtoCommissionDetailQueryParams.startDateTime).toDate(),
                    tenantDateTime.toTenantDateTime(dtoCommissionDetailQueryParams.endDateTime).toDate()
                )
            )
            query.orderBy(criteriaBuilder.desc(root.get<Date>("datetime")))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
    }

    fun getByApplicationId(applicationId: Long): CommissionDetail? {
        return getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("applicationId"), applicationId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }
}