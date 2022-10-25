package cn.sunline.saas.workflow.definition.controller

import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.workflow.defintion.modules.dto.DTOEventDefinitionView
import cn.sunline.saas.workflow.defintion.services.EventDefinitionService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("definition/event")
class EventDefinitionController {
    @Autowired
    private lateinit var eventDefinitionService: EventDefinitionService

    val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping
    fun findAll():ResponseEntity<DTOPagedResponseSuccess>{
        val paged = eventDefinitionService.findAll()
        return DTOPagedResponseSuccess(paged.map { objectMapper.convertValue<DTOEventDefinitionView>(it) }).response()
    }
}