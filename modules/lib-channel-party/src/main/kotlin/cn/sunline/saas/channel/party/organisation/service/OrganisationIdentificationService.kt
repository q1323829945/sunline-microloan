package cn.sunline.saas.channel.party.organisation.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.channel.party.organisation.model.OrganisationIdentificationType
import cn.sunline.saas.channel.party.organisation.model.db.OrganisationIdentification
import cn.sunline.saas.channel.party.organisation.repository.OrganisationIdentificationRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class OrganisationIdentificationService (private val organisationRepos: OrganisationIdentificationRepository) :
    BaseMultiTenantRepoService<OrganisationIdentification, Long>(organisationRepos) {

    fun findOrganisationId(organisationIdentificationType: OrganisationIdentificationType,organisationIdentification: String):Long?{

        val identification = getPageWithTenant({root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<OrganisationIdentificationType>("organisationIdentificationType"),organisationIdentificationType))
            predicates.add(criteriaBuilder.equal(root.get<String>("organisationIdentification"),organisationIdentification))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())

        return identification.content.firstOrNull()?.organisationId
    }
}