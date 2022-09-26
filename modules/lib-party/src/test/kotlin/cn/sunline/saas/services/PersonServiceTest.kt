package cn.sunline.saas.services

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.global.util.setTimeZone
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.party.person.model.PersonIdentificationType
import cn.sunline.saas.party.person.model.ResidentialStatus
import cn.sunline.saas.party.person.model.RoleType
import cn.sunline.saas.party.person.model.dto.*
import cn.sunline.saas.party.person.service.PersonService
import cn.sunline.saas.seq.Sequence
import org.assertj.core.api.Assertions
import org.joda.time.DateTimeZone
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonServiceTest(@Autowired val tenantService: TenantService) {
    @Autowired
    private lateinit var personService: PersonService
    @Autowired
    private lateinit var sequence: Sequence

    var id:Long = 0

    @BeforeAll
    fun `init`(){
        tenantService.save(
            Tenant(
                id = 123456,
                name = "name",
                country = CountryType.CHN,
                enabled = true,
                uuid = UUID.randomUUID()
            )
        )

        ContextUtil.setTenant("123456")

        val person = personService.register(
            DTOPersonAdd(
                id = sequence.nextId(),
                personName = DTOPersonNameAdd(
                    id = null,
                    firstName = "firstName",
                    familyName = "familyName",
                    givenName = "givenName"
                ),
                residentialStatus = ResidentialStatus.NON_PERMANENT,
                birthDate = "1999-11-11",
                nationality = CountryType.CHN,
                ethnicity = "ethnicity",
                personIdentifications = mutableListOf(
                    DTOPersonIdentificationAdd(
                        id = null,
                        personId = null,
                        personIdentificationType = PersonIdentificationType.PASSPORT_NUMBER,
                        personIdentification = "11111"
                    )
                ),
                personRoles = listOf(
                    DTOPersonRoleAdd(
                        id = null,
                        personId = null,
                        type = RoleType.OUTER,
                    )
                )
            )
        )

        id = person.id.toLong()
    }

    @Test
    @Transactional
    fun `get personPaged`(){
        val person = personService.getPersonPaged(null,null, Pageable.unpaged())

        Assertions.assertThat(person.content.size).isNotEqualTo(0)
        Assertions.assertThat(person.content[0].personName.firstName).isEqualTo("firstName")


    }


    @Test
    @Transactional
    fun `get person`(){
        val person = personService.getDetail(id)

        Assertions.assertThat(person).isNotNull
    }

    @Test
    @Transactional
    fun `update person`(){
        val newPerson = personService.updatePerson(id, DTOPersonChange(
            personName = DTOPersonNameChange(
                id = id.toString(),
                firstName = "firstName1",
                familyName = "familyName1",
                givenName = "givenName1",
            ),
            residentialStatus = ResidentialStatus.PERMANENT,
            nationality = CountryType.CHN,
            ethnicity = "e",
            personIdentifications = mutableListOf(DTOPersonIdentificationChange(
                id = null,
                personId = id.toString(),
                personIdentificationType = PersonIdentificationType.PASSPORT_NUMBER,
                personIdentification = "2222",
            )),
            personRoles = mutableListOf(DTOPersonRoleChange(
                id = null,
                personId = id.toString(),
                type = RoleType.INNER
            )),
        ))


        Assertions.assertThat(newPerson.personName.firstName).isEqualTo("firstName1")

    }
}