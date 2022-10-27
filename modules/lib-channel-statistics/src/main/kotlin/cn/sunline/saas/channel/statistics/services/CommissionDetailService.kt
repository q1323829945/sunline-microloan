package cn.sunline.saas.channel.statistics.services

import cn.sunline.saas.channel.statistics.modules.db.CommissionDetail
import cn.sunline.saas.channel.statistics.modules.dto.DTOCommissionCount
import cn.sunline.saas.channel.statistics.modules.dto.DTOCommissionDetail
import cn.sunline.saas.channel.statistics.modules.dto.DTOCommissionDetailQueryParams
import cn.sunline.saas.channel.statistics.repositories.CommissionDetailRepository
import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.criteria.Predicate
import javax.transaction.Transactional

@Service
class CommissionDetailService(
    private val CommissionDetailRepository: CommissionDetailRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime
) : BaseMultiTenantRepoService<CommissionDetail, Long>(CommissionDetailRepository) {


    @Transactional
    fun saveCommissionDetail(commissionDetails: List<CommissionDetail>) {
        commissionDetails.forEach { it.id = it.id ?: sequence.nextId() }
        save(commissionDetails)
    }

    fun getGroupByStatusCount(dtoCommissionDetailQueryParams: DTOCommissionDetailQueryParams): List<DTOCommissionCount> {
        val statusList = getAllByParams(dtoCommissionDetailQueryParams)
        val list = arrayListOf<DTOCommissionCount>()
        val channelCodeGroupBy = statusList.content.groupBy { it.channelCode }
        channelCodeGroupBy.forEach { (t, u) ->
            val statusGroupBy = u.groupBy { it.status }
            list += statusGroupBy.map { it ->
                DTOCommissionCount(
                    channelCode = t,
                    channelName = it.value.first().channelName,
                    commissionAmount = it.value.sumOf { it.commissionAmount },
                    statisticsAmount = it.value.sumOf { it.statisticsAmount }
                )
            }
        }
        return list
//        return statusList.content.groupBy { it.channelCode }.map { it ->
//            DTOCommissionCount(
//                channelCode = it.key,
//                channelName = it.value.first().channelName,
//                commissionAmount = it.value.filter { it.status == ApplyStatus.APPROVALED }
//                    .sumOf { it.commissionAmount },
//                statisticsAmount = it.value.filter { it.status == ApplyStatus.APPROVALED }.sumOf { it.statisticsAmount }
//            )
//        }
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

    fun getListByStatus(status: ApplyStatus): List<CommissionDetail> {
        return getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<ApplyStatus>("status"), status))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged()).content
    }
}