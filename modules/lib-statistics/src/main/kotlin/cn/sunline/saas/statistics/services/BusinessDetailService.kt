package cn.sunline.saas.statistics.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import org.springframework.stereotype.Service
import cn.sunline.saas.statistics.modules.db.BusinessDetail
import cn.sunline.saas.statistics.repositories.BusinessDetailRepository
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.statistics.modules.dto.DTOBusinessCount
import cn.sunline.saas.statistics.modules.dto.DTOBusinessDetail
import cn.sunline.saas.statistics.modules.dto.DTOBusinessDetailQueryParams
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*
import javax.persistence.criteria.Predicate

@Service
class BusinessDetailService (
    private val businessDetailRepository: BusinessDetailRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime
    ):BaseMultiTenantRepoService<BusinessDetail, Long>(businessDetailRepository) {

    fun saveBusinessDetail(dtoBusinessDetail: DTOBusinessDetail){
        save(
            BusinessDetail(
                id = sequence.nextId(),
                agreementId = dtoBusinessDetail.agreementId,
                customerId = dtoBusinessDetail.customerId,
                amount = dtoBusinessDetail.amount,
                currency = dtoBusinessDetail.currency,
                datetime = tenantDateTime.now().toDate()
            )
        )
    }

    fun getGroupByBusinessCount(dtoBusinessDetailQueryParams: DTOBusinessDetailQueryParams): List<DTOBusinessCount>{
        val groupBy = getAllByParams(dtoBusinessDetailQueryParams).content.groupBy { it.customerId }

        val dtoBusinessCount = mutableListOf<DTOBusinessCount>()
         groupBy.forEach { customerMap ->
            val currencyGroupBy = customerMap.value.groupBy { it.currency }
            currencyGroupBy.forEach { currencyMap ->
                dtoBusinessCount += DTOBusinessCount(customerMap.key,currencyMap.value.sumOf { it.amount },currencyMap.value.first().currency)
            }
        }

        return dtoBusinessCount
    }

    private fun getAllByParams(dtoBusinessDetailQueryParams: DTOBusinessDetailQueryParams):Page<BusinessDetail>{
        return getPageWithTenant({ root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.between(root.get("datetime"),dtoBusinessDetailQueryParams.startDateTime,dtoBusinessDetailQueryParams.endDateTime))
            query.orderBy(criteriaBuilder.desc(root.get<Date>("datetime")))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
    }
}