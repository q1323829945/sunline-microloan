package cn.sunline.saas.organisation.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.organisation.model.Organisation
import cn.sunline.saas.organisation.repository.OrganisationRepository
import org.springframework.stereotype.Service

/**
 * @title: OrganisationService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/14 15:55
 */
@Service
class OrganisationService(private val organisationRepos: OrganisationRepository) :
    BaseMultiTenantRepoService<Organisation, Long>(organisationRepos) {}