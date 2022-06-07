package cn.sunline.saas.party.organisation.service

import cn.sunline.saas.global.constant.PartyType
import cn.sunline.saas.party.organisation.model.dto.DTOOrganisationAdd
import cn.sunline.saas.party.organisation.model.dto.DTOOrganisationView
import cn.sunline.saas.rpc.pubsub.PartyPublish
import cn.sunline.saas.rpc.pubsub.dto.DTOCustomerDetail
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrganisationManagerService(
    private val partyPublish: PartyPublish
) {
    @Autowired
    private lateinit var organisationService: OrganisationService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun register(dtoOrganisationAdd: DTOOrganisationAdd):DTOOrganisationView{
        val organisation = organisationService.registerOrganisation(dtoOrganisationAdd)

        partyPublish.addCustomerDetail(
            DTOCustomerDetail(
                partyId = organisation.id.toLong(),
                partyType = PartyType.ORGANISATION
            )
        )

        return objectMapper.convertValue(organisation)
    }
}