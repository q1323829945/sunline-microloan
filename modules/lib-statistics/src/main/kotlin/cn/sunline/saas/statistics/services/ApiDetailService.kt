package cn.sunline.saas.statistics.services

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.statistics.modules.db.ApiDetail
import cn.sunline.saas.statistics.repositories.ApiDetailRepository
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.statistics.modules.dto.DTOApiDetailApiCount
import cn.sunline.saas.statistics.modules.dto.DTOApiDetailQueryDate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Date
import javax.persistence.criteria.Order
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

    fun getGroupByApiCount(dtoApiDetailQueryDate: DTOApiDetailQueryDate):List<DTOApiDetailApiCount>{
        val apiList = getApiByParams(dtoApiDetailQueryDate)
        val apiGroupBy = apiList.content.groupBy { it.api }
        return apiGroupBy.map {
            DTOApiDetailApiCount(it.key,it.value.count().toLong())
        }
    }


    private fun getApiByParams(dtoApiDetailQueryDate: DTOApiDetailQueryDate):Page<ApiDetail>{
        return getPaged({ root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.between(root.get("datetime"),dtoApiDetailQueryDate.startDateTime,dtoApiDetailQueryDate.endDateTime))
            predicates.add(criteriaBuilder.equal(root.get<String>("tenantId"), dtoApiDetailQueryDate.tenantId?:ContextUtil.getTenant()))
            query.orderBy(criteriaBuilder.desc(root.get<Date>("datetime")))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())
    }
}