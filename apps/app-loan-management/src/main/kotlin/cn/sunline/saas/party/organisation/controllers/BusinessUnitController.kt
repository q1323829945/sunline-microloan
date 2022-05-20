package cn.sunline.saas.party.organisation.controllers

import cn.sunline.saas.party.organisation.service.dto.DTOBusinessUnit
import cn.sunline.saas.party.organisation.exception.BusinessUnitNotFoundException
import cn.sunline.saas.party.organisation.service.BusinessUnitManagerService
import cn.sunline.saas.party.organisation.service.BusinessUnitService
import cn.sunline.saas.party.organisation.service.dto.DTOBusinessUnitPaged
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("BusinessUnit")
class BusinessUnitController {

    @Autowired
    private lateinit var businessUnitService:BusinessUnitService

    @Autowired
    private lateinit var businessUnitManagerService:BusinessUnitManagerService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @GetMapping("{id}")
    fun getOne(@PathVariable("id") id:String): DTOBusinessUnit {
        val businessUnit = businessUnitService.getOne(id.toLong())?: throw BusinessUnitNotFoundException("Invalid businessUnit")
        return objectMapper.convertValue(businessUnit)
    }

    @GetMapping
    fun getAll():ResponseEntity<DTOResponseSuccess<List<DTOBusinessUnitPaged>>>{
        return DTOResponseSuccess(businessUnitManagerService.getList()).response()
    }

}