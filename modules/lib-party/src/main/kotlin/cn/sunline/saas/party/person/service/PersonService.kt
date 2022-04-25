package cn.sunline.saas.party.person.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.party.person.model.Person
import cn.sunline.saas.party.person.repository.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonService(private val personRepository: PersonRepository) :
    BaseMultiTenantRepoService<Person, Long>(personRepository) {
}