package cn.sunline.saas.workflow.definition.controller

import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.workflow.definition.service.AppActivityDefinitionService
import cn.sunline.saas.workflow.defintion.modules.dto.DTOActivityDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOActivityDefinitionView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("definition/activity")
class ActivityDefinitionController {
    @Autowired
    private lateinit var appActivityDefinitionService: AppActivityDefinitionService

    @PostMapping
    fun add(@RequestBody dtoActivityDefinition: DTOActivityDefinition):ResponseEntity<DTOResponseSuccess<DTOActivityDefinitionView>>{
        val activity = appActivityDefinitionService.addOne(dtoActivityDefinition)
        return DTOResponseSuccess(activity).response()
    }

    @PutMapping("{id}")
    fun update(@PathVariable id:Long, @RequestBody dtoActivityDefinition: DTOActivityDefinition):ResponseEntity<DTOResponseSuccess<DTOActivityDefinitionView>>{
        val activity = appActivityDefinitionService.updateOne(id, dtoActivityDefinition)
        return DTOResponseSuccess(activity).response()
    }

    @GetMapping("{processId}")
    fun paged(@PathVariable processId:Long,pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = appActivityDefinitionService.getPaged(processId,pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id:Long):ResponseEntity<DTOResponseSuccess<DTOActivityDefinitionView>>{
        val activity = appActivityDefinitionService.delete(id)
        return DTOResponseSuccess(activity).response()
    }
}