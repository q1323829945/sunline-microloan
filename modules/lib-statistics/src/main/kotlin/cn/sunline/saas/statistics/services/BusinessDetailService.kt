package cn.sunline.saas.statistics.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service
import cn.sunline.saas.statistics.modules.db.BusinessDetail
import cn.sunline.saas.statistics.repositories.BusinessDetailRepository
import cn.sunline.saas.seq.Sequence

@Service
class BusinessDetailService (
    private val businessDetailRepository: BusinessDetailRepository,
    private val sequence: Sequence
    ):BaseMultiTenantRepoService<BusinessDetail, Long>(businessDetailRepository) {

    fun saveBusinessDetail(){

    }
}