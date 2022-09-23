package cn.sunline.saas.party.person.service

import cn.sunline.saas.global.constant.PartyType
import cn.sunline.saas.party.person.model.dto.DTOPersonAdd
import cn.sunline.saas.party.person.model.dto.DTOPersonView
import cn.sunline.saas.rpc.pubsub.PartyPublish
import cn.sunline.saas.rpc.pubsub.dto.DTOCustomerDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PersonManagerService(
    private val partyPublish: PartyPublish
) {
    @Autowired
    private lateinit var personService: PersonService

    fun register(dtoPersonAdd: DTOPersonAdd): DTOPersonView {
        val person = personService.getOne(dtoPersonAdd.id)
        return if(person == null){
            val personView = personService.register(dtoPersonAdd)

            partyPublish.addCustomerDetail(
                DTOCustomerDetail(
                    partyId = personView.id.toLong(),
                    partyType = PartyType.PERSON
                )
            )
            personView
        }else{
            personService.getDTOPersonView(person)
        }
    }
}