package cn.sunline.saas.party.person.controllers

import cn.sunline.saas.party.person.model.RoleType
import cn.sunline.saas.party.person.model.dto.DTOPersonAdd
import cn.sunline.saas.party.person.model.dto.DTOPersonChange
import cn.sunline.saas.party.person.model.dto.DTOPersonView
import cn.sunline.saas.party.person.service.PersonManagerService
import cn.sunline.saas.party.person.service.PersonService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
@RequestMapping("Person")
class PersonController {
    @Autowired
    private lateinit var personService: PersonService

    @Autowired
    private lateinit var personManagerService: PersonManagerService

    @GetMapping
    fun getPaged(@PathParam("personIdentification")personIdentification: String?,
                 @PathParam("type")type: RoleType?,
                 pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = personService.getPersonPaged(personIdentification,type,pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @PostMapping
    fun register(@RequestBody dtoPersonAdd: DTOPersonAdd):ResponseEntity<DTOResponseSuccess<DTOPersonView>>{
        val person = personManagerService.register(dtoPersonAdd)
        return DTOResponseSuccess(person).response()
    }

    @PutMapping("{id}")
    fun update(@PathVariable id:String,@RequestBody dtoPersonChange: DTOPersonChange):ResponseEntity<DTOResponseSuccess<DTOPersonView>>{
        val person = personService.updatePerson(id.toLong(),dtoPersonChange)
        return DTOResponseSuccess(person).response()
    }

    @GetMapping("{id}")
    fun getDetail(@PathVariable id:String):ResponseEntity<DTOResponseSuccess<DTOPersonView>>{
        val person = personService.getDetail(id.toLong())
        return DTOResponseSuccess(person).response()
    }
}