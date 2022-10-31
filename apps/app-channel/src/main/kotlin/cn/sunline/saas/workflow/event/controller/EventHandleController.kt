package cn.sunline.saas.workflow.event.controller

import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.workflow.event.service.EventHandleService
import cn.sunline.saas.workflow.event.service.dto.DTOEventHandle
import cn.sunline.saas.workflow.event.service.dto.DTOEventHandleDetail
import cn.sunline.saas.workflow.step.modules.StepStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam

@RestController
@RequestMapping("event/handle")
class EventHandleController {
    @Autowired
    private lateinit var eventHandleService: EventHandleService

    @GetMapping
    fun paged(@PathParam("status")status: StepStatus?,pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = eventHandleService.getEventHandlePaged(null,status, pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @GetMapping("user")
    fun userPaged(@RequestHeader(required = true, name = "X-Authorization-Username") user: String, @PathParam("status")status: StepStatus?, pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = eventHandleService.getEventHandlePaged(user,status, pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id:Long,@RequestBody dtoEventHandle: DTOEventHandle){
        eventHandleService.updateEventStep(id, dtoEventHandle)
    }

    @GetMapping("{id}")
    fun detail(@PathVariable id:Long):ResponseEntity<DTOResponseSuccess<DTOEventHandleDetail>>{
        val event = eventHandleService.detail(id)
        return DTOResponseSuccess(event).response()
    }
}