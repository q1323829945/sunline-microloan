package cn.sunline.saas.workflow.definition.controller

import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.workflow.definition.service.AppProcessDefinitionService
import cn.sunline.saas.workflow.defintion.modules.DefinitionStatus
import cn.sunline.saas.workflow.defintion.modules.dto.DTOProcessDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOProcessDefinitionView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam

@RestController
@RequestMapping("definition/process")
class ProcessDefinitionController {
    @Autowired
    private lateinit var appProcessDefinitionService: AppProcessDefinitionService

    @PostMapping
    fun addOne(@RequestBody dtoProcessDefinition: DTOProcessDefinition):ResponseEntity<DTOResponseSuccess<DTOProcessDefinitionView>>{
        val response = appProcessDefinitionService.addOne(dtoProcessDefinition)
        return DTOResponseSuccess(response).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id:Long,@RequestBody dtoProcessDefinition: DTOProcessDefinition):ResponseEntity<DTOResponseSuccess<DTOProcessDefinitionView>>{
        val response = appProcessDefinitionService.updateOne(id, dtoProcessDefinition)
        return DTOResponseSuccess(response).response()
    }

    @GetMapping
    fun paged(@PathParam(value = "status")status:DefinitionStatus? = null,pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = appProcessDefinitionService.getPaged(status, pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @PutMapping("{status}/{id}")
    fun updateStatus(@PathVariable id:Long,@PathVariable status:DefinitionStatus):ResponseEntity<DTOResponseSuccess<DTOProcessDefinitionView>>{
        val response = appProcessDefinitionService.updateStatus(id, status)
        return DTOResponseSuccess(response).response()
    }



}