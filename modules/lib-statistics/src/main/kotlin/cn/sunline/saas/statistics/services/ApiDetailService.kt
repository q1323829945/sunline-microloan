package cn.sunline.saas.statistics.services

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.statistics.modules.db.ApiDetail
import cn.sunline.saas.statistics.modules.dto.DTOApiDetail
import cn.sunline.saas.statistics.repositories.ApiDetailRepository
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.statistics.modules.dto.DTOApiDetailQueryCountParams
import java.util.Date
import javax.persistence.criteria.Predicate

@Service
class ApiDetailService (
    private val apiDetailRepository: ApiDetailRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime
    ):BaseMultiTenantRepoService<ApiDetail, Long>(apiDetailRepository) {

    fun saveApiDetail(dtoApiDetail: DTOApiDetail){
        save(
            ApiDetail(
                id = sequence.nextId(),
                api = dtoApiDetail.api,
                datetime = tenantDateTime.now().toDate()
            )
        )
    }

    fun getCount(dtoApiDetailQueryCountParams: DTOApiDetailQueryCountParams):Long{
        return apiDetailRepository.count { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<String>("api"), dtoApiDetailQueryCountParams.api))
            predicates.add(criteriaBuilder.equal(root.get<String>("tenantId"), ContextUtil.getTenant()))
            predicates.add(criteriaBuilder.between(root.get("datetime"),dtoApiDetailQueryCountParams.startDateTime,dtoApiDetailQueryCountParams.endDateTime))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }
}