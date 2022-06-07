package cn.sunline.saas.statistics.services

import cn.sunline.saas.global.constant.PartyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.statistics.modules.db.CustomerDetail
import cn.sunline.saas.statistics.modules.dto.DTOBusinessDetailQueryParams
import cn.sunline.saas.statistics.modules.dto.DTOCustomerCount
import cn.sunline.saas.statistics.modules.dto.DTOCustomerDetail
import cn.sunline.saas.statistics.repositories.CustomerDetailRepository
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

    fun getGroupByCustomerCount(dtoBusinessDetailQueryParams: DTOBusinessDetailQueryParams):List<DTOCustomerCount>{
        val customerGroupBy = getAllByParams(dtoBusinessDetailQueryParams).content.groupBy { it.getTenantId() }

        return customerGroupBy.map { (t, u) ->
            DTOCustomerCount(tenantId = t,
                personCount = u.count { it.partyType == PartyType.PERSON }.toLong(),
                organisationCount = u.count { it.partyType == PartyType.ORGANISATION }.toLong(),
                partyCount = u.count().toLong()
            )
        }


    }


    private fun getAllByParams(dtoBusinessDetailQueryParams: DTOBusinessDetailQueryParams):Page<CustomerDetail>{
        return getPaged({ root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.between(root.get("datetime"),dtoBusinessDetailQueryParams.startDateTime,dtoBusinessDetailQueryParams.endDateTime))
            predicates.add(criteriaBuilder.equal(root.get<String>("tenantId"), dtoBusinessDetailQueryParams.tenantId?: ContextUtil.getTenant()))
            query.orderBy(criteriaBuilder.desc(root.get<Date>("datetime")))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
    }

}