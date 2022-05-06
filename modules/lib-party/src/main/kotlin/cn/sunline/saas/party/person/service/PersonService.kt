package cn.sunline.saas.party.person.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.party.person.exception.PersonNotFoundException
import cn.sunline.saas.party.person.model.db.Person
import cn.sunline.saas.party.person.model.dto.DTOPersonAdd
import cn.sunline.saas.party.person.model.dto.DTOPersonChange
import cn.sunline.saas.party.person.model.dto.DTOPersonView
import cn.sunline.saas.party.person.repository.PersonRepository
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.module.kotlin.convertValue
import org.joda.time.Instant
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@Service
class PersonService(private val personRepository: PersonRepository) :
    BaseMultiTenantRepoService<Person, Long>(personRepository) {
    @Autowired
    private lateinit var sequence: Sequence

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun register(dtoPersonAdd: DTOPersonAdd):DTOPersonView{
        val id = sequence.nextId()
        dtoPersonAdd.personName.id = id
        val person = Person(
            id = id,
            personName = objectMapper.convertValue(dtoPersonAdd.personName),
            residentialStatus = dtoPersonAdd.residentialStatus,
            birthDate = Instant.parse(dtoPersonAdd.birthDate),
            nationality = dtoPersonAdd.nationality,
            ethnicity = dtoPersonAdd.ethnicity,
        )
        dtoPersonAdd.personIdentifications.forEach {
            it.id = sequence.nextId()
            it.personId = id
        }
        person.personIdentifications = objectMapper.convertValue(dtoPersonAdd.personIdentifications)

        dtoPersonAdd.personRoles.forEach  {
            it.id = sequence.nextId()
            it.personId = id
        }
        person.personRoles = objectMapper.convertValue(dtoPersonAdd.personRoles)

        return getDTOPersonView(save(person))
    }

    fun getDetail(id:Long):DTOPersonView{
        val person = getOne(id)?: throw PersonNotFoundException("Invalid person")

        return getDTOPersonView(save(person))
    }

    fun updatePerson(id:Long,dtoPersonChange: DTOPersonChange):DTOPersonView{
        val oldOne = getOne(id)?: throw PersonNotFoundException("Invalid person")
        dtoPersonChange.personIdentifications.forEach {
            it.id?:run {
                it.id = sequence.nextId().toString()
                it.personId = id.toString()
            }
        }
        dtoPersonChange.personRoles.forEach {
            it.id?:run {
                it.id = sequence.nextId().toString()
                it.personId = id.toString()
            }
        }

        oldOne.personName = objectMapper.convertValue(dtoPersonChange.personName)
        oldOne.personIdentifications = objectMapper.convertValue(dtoPersonChange.personIdentifications)
        oldOne.personRoles = objectMapper.convertValue(dtoPersonChange.personRoles)
        oldOne.ethnicity = dtoPersonChange.ethnicity
        oldOne.nationality = dtoPersonChange.nationality
        oldOne.residentialStatus = dtoPersonChange.residentialStatus

        return getDTOPersonView(save(oldOne))
    }

    fun getPersonPaged(pageable: Pageable):Page<DTOPersonView>{
        return getPageWithTenant(null,pageable).map {
            getDTOPersonView(it)
        }
    }

    fun getDTOPersonView(person: Person):DTOPersonView{
        return DTOPersonView(
            id = person.id.toString(),
            personName = objectMapper.convertValue(person.personName),
            residentialStatus = person.residentialStatus,
            birthDate = person.birthDate.toDateTime().toString("yyyy-MM-dd"),
            nationality = person.nationality,
            ethnicity = person.ethnicity,
            personIdentifications = objectMapper.convertValue(person.personIdentifications),
            personRoles = objectMapper.convertValue(person.personRoles)
        )
    }
}
