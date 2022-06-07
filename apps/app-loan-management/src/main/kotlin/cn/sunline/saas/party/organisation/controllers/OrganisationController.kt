package cn.sunline.saas.party.organisation.controllers

import cn.sunline.saas.party.organisation.model.dto.DTOOrganisationAdd
import cn.sunline.saas.party.organisation.model.dto.DTOOrganisationChange
import cn.sunline.saas.party.organisation.model.dto.DTOOrganisationView
import cn.sunline.saas.party.organisation.service.OrganisationManagerService
import cn.sunline.saas.party.organisation.service.OrganisationService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
@RequestMapping("Organisation")
class OrganisationController {
    @Autowired
    private lateinit var organisationService: OrganisationService

    @Autowired
    private lateinit var organisationManagerService: OrganisationManagerService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @GetMapping
    fun getPaged(@PathParam("legalEntityIndicator")legalEntityIndicator:String?,
                 @PathParam("organisationIdentification")organisationIdentification: String?,
                 pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        return DTOPagedResponseSuccess(organisationService.getOrganisationPaged(legalEntityIndicator,organisationIdentification,pageable).map { it }).response()
    }

    @PostMapping
    fun register(@RequestBody dtoOrganisationAdd: DTOOrganisationAdd):ResponseEntity<DTOResponseSuccess<DTOOrganisationView>>{
        val organisation = organisationManagerService.register(dtoOrganisationAdd)
        return DTOResponseSuccess(organisation).response()
    }

    @PutMapping("{id}")
    fun update(@PathVariable id:String,@RequestBody dtoOrganisationChange: DTOOrganisationChange):ResponseEntity<DTOResponseSuccess<DTOOrganisationView>>{
        val organisation = organisationService.updateOrganisation(id.toLong(),dtoOrganisationChange)
        val responseEntity = objectMapper.convertValue<DTOOrganisationView>(organisation)
        return DTOResponseSuccess(responseEntity).response()
    }

    @GetMapping("{id}")
    fun getDetail(@PathVariable id:String):ResponseEntity<DTOResponseSuccess<DTOOrganisationView>>{
        val organisation = organisationService.getDetail(id.toLong())
        val responseEntity = objectMapper.convertValue<DTOOrganisationView>(organisation)
        return DTOResponseSuccess(responseEntity).response()
    }
}