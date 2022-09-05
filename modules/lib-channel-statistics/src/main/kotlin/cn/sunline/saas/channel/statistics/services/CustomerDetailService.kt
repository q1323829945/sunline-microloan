package cn.sunline.saas.channel.statistics.services

import cn.sunline.saas.global.constant.PartyType
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.channel.statistics.modules.db.CustomerDetail
import cn.sunline.saas.channel.statistics.modules.dto.DTOCustomerCount
import cn.sunline.saas.channel.statistics.modules.dto.DTOCustomerDetail
import cn.sunline.saas.channel.statistics.modules.dto.DTOCustomerDetailQueryParams
import cn.sunline.saas.channel.statistics.repositories.CustomerDetailRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class CustomerDetailService (
    private val customerDetailRepository: CustomerDetailRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime
    ):BaseMultiTenantRepoService<CustomerDetail, Long>(customerDetailRepository) {

    fun saveCustomerDetail(dtoCustomerDetail: DTOCustomerDetail){
        save(
            CustomerDetail(
                id = sequence.nextId(),
                partyId = dtoCustomerDetail.partyId,
                partyType = dtoCustomerDetail.partyType,
                datetime = tenantDateTime.now().toDate()
            )
        )
    }

    fun getGroupByCustomerCount(dtoCustomerDetailQueryParams: DTOCustomerDetailQueryParams):List<DTOCustomerCount>{
        val customerGroupBy = getAllByParams(dtoCustomerDetailQueryParams).content.groupBy { it.getTenantId() }

        return customerGroupBy.map { (t, u) ->
            DTOCustomerCount(tenantId = t,
                personCount = u.count { it.partyType == PartyType.PERSON }.toLong(),
                organisationCount = u.count { it.partyType == PartyType.ORGANISATION }.toLong(),
                partyCount = u.count().toLong()
            )
        }


    }


    private fun getAllByParams(dtoCustomerDetailQueryParams: DTOCustomerDetailQueryParams):Page<CustomerDetail>{
        return getPageWithTenant({ root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.between(root.get("datetime"),dtoCustomerDetailQueryParams.startDateTime,dtoCustomerDetailQueryParams.endDateTime))
            query.orderBy(criteriaBuilder.desc(root.get<Date>("datetime")))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
    }

    fun getOneByPartyIdAndPartyType(partyId: Long,partyType: PartyType): CustomerDetail?{
        return getOneWithTenant { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("partyId"), partyId))
            predicates.add(criteriaBuilder.equal(root.get<PartyType>("partyType"), partyType))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }
}