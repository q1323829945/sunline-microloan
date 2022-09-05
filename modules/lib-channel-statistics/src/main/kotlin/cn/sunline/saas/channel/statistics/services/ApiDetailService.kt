package cn.sunline.saas.channel.statistics.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.statistics.modules.db.ApiDetail
import cn.sunline.saas.channel.statistics.repositories.ApiDetailRepository
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.channel.statistics.modules.dto.DTOApiCount
import cn.sunline.saas.channel.statistics.modules.dto.DTOApiDetailQueryParams
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Date
import javax.persistence.criteria.Predicate

@Service
class ApiDetailService (
    private val apiDetailRepository: ApiDetailRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime
    ):BaseMultiTenantRepoService<ApiDetail, Long>(apiDetailRepository) {

    fun saveApiDetail(api:String){
        save(
            ApiDetail(
                id = sequence.nextId(),
                api = api,
                datetime = tenantDateTime.now().toDate()
            )
        )
    }

    fun getGroupByApiCount(dtoApiDetailQueryParams: DTOApiDetailQueryParams):List<DTOApiCount>{
        val apiList = getAllByParams(dtoApiDetailQueryParams)
        val apiGroupBy = apiList.content.groupBy { it.api }
        return apiGroupBy.map {
            DTOApiCount(it.key,it.value.count().toLong())
        }
    }


    private fun getAllByParams(dtoApiDetailQueryParams: DTOApiDetailQueryParams):Page<ApiDetail>{
        return getPageWithTenant({ root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.between(root.get("datetime"),dtoApiDetailQueryParams.startDateTime,dtoApiDetailQueryParams.endDateTime))
            query.orderBy(criteriaBuilder.desc(root.get<Date>("datetime")))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
    }
}