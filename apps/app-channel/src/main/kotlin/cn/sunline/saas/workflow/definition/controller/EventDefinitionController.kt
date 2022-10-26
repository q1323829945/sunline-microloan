package cn.sunline.saas.workflow.definition.controller

import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.workflow.definition.service.AppEventDefinitionService
import cn.sunline.saas.workflow.defintion.modules.dto.DTOActivityDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOActivityDefinitionView
import cn.sunline.saas.workflow.defintion.modules.dto.DTOEventDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOEventDefinitionView
import cn.sunline.saas.workflow.defintion.services.EventDefinitionService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("definition/event")
class EventDefinitionController {
    @Autowired
    private lateinit var eventDefinitionService: AppEventDefinitionService

    @PostMapping
    fun addOne(@RequestBody dtoEventDefinition: DTOEventDefinition):ResponseEntity<DTOResponseSuccess<DTOEventDefinitionView>>{
        val event = eventDefinitionService.addOne(dtoEventDefinition)
        return DTOResponseSuccess(event).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id:Long, @RequestBody dtoEventDefinition: DTOEventDefinition):ResponseEntity<DTOResponseSuccess<DTOEventDefinitionView>>{
        val event = eventDefinitionService.updateOne(id, dtoEventDefinition)
        return DTOResponseSuccess(event).response()
    }

    @GetMapping("{activityId}")
    fun paged(@PathVariable activityId:Long, pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = eventDefinitionService.getPaged(activityId, pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }
}