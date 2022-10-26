package cn.sunline.saas.workflow.step.controller

import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.workflow.step.service.AppActivityStepService
import cn.sunline.saas.workflow.step.service.AppEventStepService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("step/activity")
class ActivityStepController {
    @Autowired
    private lateinit var appActivityStepService: AppActivityStepService

    @GetMapping("{processStepId}")
    fun paged(@PathVariable processStepId:Long,pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val paged = appActivityStepService.paged(processStepId, pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }
}