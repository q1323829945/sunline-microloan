package cn.sunline.saas.workflow.step.controller

import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.service.AppProcessStepService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
@RequestMapping("step/process")
class ProcessStepController {
    @Autowired
    private lateinit var processStepService: AppProcessStepService

    @GetMapping
    fun paged(@PathParam(value = "name")name:String?,
              @PathParam(value = "status")status:StepStatus?,
              pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = processStepService.paged(name, status, pageable)

        return DTOPagedResponseSuccess(paged.map { it }).response()
    }
}