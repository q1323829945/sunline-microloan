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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("definition/activity")
class ActivityDefinitionController {
    @Autowired
    private lateinit var appActivityDefinitionService: AppActivityDefinitionService

    @PostMapping
    fun addOne(@RequestBody dtoActivityDefinition: DTOActivityDefinition):ResponseEntity<DTOResponseSuccess<DTOActivityDefinitionView>>{
        val activity = appActivityDefinitionService.addOne(dtoActivityDefinition)
        return DTOResponseSuccess(activity).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id:Long, @RequestBody dtoActivityDefinition: DTOActivityDefinition):ResponseEntity<DTOResponseSuccess<DTOActivityDefinitionView>>{
        val activity = appActivityDefinitionService.updateOne(id, dtoActivityDefinition)
        return DTOResponseSuccess(activity).response()
    }

    @GetMapping("{processId}")
    fun paged(@PathVariable processId:Long,pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = appActivityDefinitionService.getPaged(processId,pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }
}