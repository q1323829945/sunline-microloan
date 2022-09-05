package cn.sunline.saas.channel.statistics.services

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.channel.statistics.modules.db.CommissionDetail
import cn.sunline.saas.channel.statistics.modules.dto.DTOCommissionCount
import cn.sunline.saas.channel.statistics.modules.dto.DTOCommissionDetail
import cn.sunline.saas.channel.statistics.modules.dto.DTOCommissionDetailQueryParams
import cn.sunline.saas.channel.statistics.repositories.CommissionDetailRepository
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
                channelCode = dtoCommissionDetail.channelCode,
                channelName = dtoCommissionDetail.channelName,
                applicationId = dtoCommissionDetail.applicationId,
                amount = dtoCommissionDetail.amount,
                datetime = tenantDateTime.now().toDate(),
                status = dtoCommissionDetail.status
            )
        )
    }

    fun getGroupByStatusCount(dtoCommissionDetailQueryParams: DTOCommissionDetailQueryParams): List<DTOCommissionCount> {
        val statusList = getAllByParams(dtoCommissionDetailQueryParams)
        val dtoCommissionCounts = ArrayList<DTOCommissionCount>()
        statusList.content.groupBy { it.channelCode }.forEach { channelCodeMap ->
            dtoCommissionCounts += DTOCommissionCount(
                channelCode = channelCodeMap.key,
                channelName = channelCodeMap.value.first().channelName,
                amount = channelCodeMap.value.filter { it.status == ApplyStatus.APPROVALED }.sumOf { it.amount })
        }
        return dtoCommissionCounts
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

    fun getByApplicationId(applicationId: String): CommissionDetail? {
        return getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("applicationId"), applicationId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }
}