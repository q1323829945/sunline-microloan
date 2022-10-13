package cn.sunline.saas.interest.controller

import cn.sunline.saas.interest.controller.dto.*
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.interest.service.RatePlanManagerService
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

@RestController
@RequestMapping("RatePlan")
class RatePlanController {

    @Autowired
    private lateinit var ratePlanManagerService: RatePlanManagerService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = ratePlanManagerService.getPaged(pageable = pageable)
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTORatePlan>(it) }).response()
    }

    @GetMapping("all")
    fun getAll(@RequestParam("type")type:RatePlanType): ResponseEntity<DTOResponseSuccess<List<DTORatePlan>>> {
        val page = ratePlanManagerService.getAll(type)
        return DTOResponseSuccess(page.map { objectMapper.convertValue<DTORatePlan>(it) }).response()
    }

    @GetMapping("all/custom")
    fun getAllCustomer(): ResponseEntity<DTOResponseSuccess<List<DTORatePlan>>> {
        return DTOResponseSuccess(ratePlanManagerService.getAllCustomRatePlan().map { objectMapper.convertValue<DTORatePlan>(it) }).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtoRatePlan: DTORatePlan): ResponseEntity<DTOResponseSuccess<DTORatePlanWithInterestRates>> {
        return DTOResponseSuccess(ratePlanManagerService.addOne(dtoRatePlan)).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoRatePlan: DTORatePlan): ResponseEntity<DTOResponseSuccess<DTORatePlanWithInterestRates>> {
        return DTOResponseSuccess(ratePlanManagerService.updateOne(id,dtoRatePlan)).response()
    }

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long): ResponseEntity<DTOResponseSuccess<DTORatePlanWithInterestRates>> {
        return DTOResponseSuccess(ratePlanManagerService.getOne(id)).response()
    }

    @GetMapping("invokeAll")
    fun getInvokeAll(@RequestParam("type")type:RatePlanType): ResponseEntity<DTOResponseSuccess<DTORatePlanWithInterestRates>>{
        val page = ratePlanManagerService.getInvokeAll(type)
        val map = page.map { objectMapper.convertValue<DTORatePlanWithInterestRates>(it) }.first()
        return DTOResponseSuccess(map).response()
    }

}