package cn.sunline.saas.party.person.service

import cn.sunline.saas.global.constant.PartyType
import cn.sunline.saas.party.organisation.model.dto.DTOOrganisationAdd
import cn.sunline.saas.party.organisation.model.dto.DTOOrganisationView
import cn.sunline.saas.party.organisation.service.dto.DTOBusinessUnitPaged
import cn.sunline.saas.party.person.model.dto.DTOPersonAdd
import cn.sunline.saas.party.person.model.dto.DTOPersonView
import cn.sunline.saas.rpc.pubsub.PartyPublish
import cn.sunline.saas.rpc.pubsub.dto.DTOCustomerDetail
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PersonManagerService(
    private val partyPublish: PartyPublish
) {
    @Autowired
    private lateinit var personService: PersonService

    fun register(dtoPersonAdd: DTOPersonAdd): DTOPersonView {
        val person = personService.register(dtoPersonAdd)

        partyPublish.addCustomerDetail(
            DTOCustomerDetail(
                partyId = person.id.toLong(),
                partyType = PartyType.PERSON
            )
        )

        return person
    }
}