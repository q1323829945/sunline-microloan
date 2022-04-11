package cn.sunline.saas.interest.controller

import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.interest.dto.DTORatePlanAdd
import cn.sunline.saas.interest.dto.DTORatePlanChange
import cn.sunline.saas.interest.dto.DTORatePlanView
import cn.sunline.saas.interest.exception.RatePlanNotFoundException
import cn.sunline.saas.interest.model.RatePlan
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.interest.service.RatePlanManagerService
import cn.sunline.saas.interest.service.RatePlanService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.persistence.criteria.Predicate

@RestController
@RequestMapping("RatePlan")
class RatePlanController {

    @Autowired
    private lateinit var ratePlanManagerService: RatePlanManagerService


    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        return ratePlanManagerService.getPaged(pageable = pageable)
    }

    @GetMapping("all")
    fun getAll(@RequestParam("type")type:RatePlanType,pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        return ratePlanManagerService.getAll(type,pageable)
    }

    @PostMapping
    fun addOne(@RequestBody dtoRatePlan: DTORatePlanAdd): ResponseEntity<DTOResponseSuccess<DTORatePlanView>> {
        return ratePlanManagerService.addOne(dtoRatePlan)
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoRatePlan: DTORatePlanChange): ResponseEntity<DTOResponseSuccess<DTORatePlanView>> {
        return ratePlanManagerService.updateOne(id,dtoRatePlan)
    }

}