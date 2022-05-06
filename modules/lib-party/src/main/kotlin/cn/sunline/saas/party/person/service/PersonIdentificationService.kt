package cn.sunline.saas.party.person.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.party.person.model.PersonIdentificationType
import cn.sunline.saas.party.person.model.db.PersonIdentification
import cn.sunline.saas.party.person.repository.PersonIdentificationRepository
import org.springframework.data.domain.Pageable
import javax.persistence.criteria.Predicate

class PersonIdentificationService (private val personIdentificationRepository: PersonIdentificationRepository) :
    BaseMultiTenantRepoService<PersonIdentification, Long>(personIdentificationRepository) {

    fun findPersonId(personIdentificationType: PersonIdentificationType,personIdentification: String):Long?{

        val identification = getPageWithTenant({root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<PersonIdentificationType>("personIdentificationType"),personIdentificationType))
            predicates.add(criteriaBuilder.equal(root.get<String>("personIdentification"),personIdentification))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())

        return identification.content.firstOrNull()?.personId
    }
}