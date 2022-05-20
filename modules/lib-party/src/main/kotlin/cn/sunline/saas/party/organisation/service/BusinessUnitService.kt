package cn.sunline.saas.party.organisation.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.party.organisation.model.db.BusinessUnit
import cn.sunline.saas.party.organisation.repository.BusinessUnitRepository
import org.springframework.stereotype.Service

@Service
class BusinessUnitService (private val businessUnitRepos: BusinessUnitRepository) :
    BaseMultiTenantRepoService<BusinessUnit, Long>(businessUnitRepos) {


}