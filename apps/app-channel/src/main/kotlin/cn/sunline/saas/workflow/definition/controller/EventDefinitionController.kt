package cn.sunline.saas.workflow.definition.controller

import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.workflow.definition.service.AppEventDefinitionService
import cn.sunline.saas.workflow.defintion.modules.dto.DTOEventDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOEventDefinitionView
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
    fun add(@RequestBody dtoEventDefinition: DTOEventDefinition):ResponseEntity<DTOResponseSuccess<DTOEventDefinitionView>>{
        val event = eventDefinitionService.addOne(dtoEventDefinition)
        return DTOResponseSuccess(event).response()
    }

    @PutMapping("{id}")
    fun update(@PathVariable id:Long, @RequestBody dtoEventDefinition: DTOEventDefinition):ResponseEntity<DTOResponseSuccess<DTOEventDefinitionView>>{
        val event = eventDefinitionService.updateOne(id, dtoEventDefinition)
        return DTOResponseSuccess(event).response()
    }

    @GetMapping("{activityId}")
    fun paged(@PathVariable activityId:Long, pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = eventDefinitionService.getPaged(activityId, pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id:Long):ResponseEntity<DTOResponseSuccess<DTOEventDefinitionView>>{
        val event = eventDefinitionService.delete(id)
        return DTOResponseSuccess(event).response()
    }
}