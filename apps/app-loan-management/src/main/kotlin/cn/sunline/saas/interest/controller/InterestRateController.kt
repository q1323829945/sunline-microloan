package cn.sunline.saas.interest.controller

import cn.sunline.saas.interest.controller.dto.DTOInterestRate
import cn.sunline.saas.interest.service.InterestRateManagerService
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
@RequestMapping("InterestRate")
class InterestRateController {


    @Autowired
    private lateinit var interestRateManagerService: InterestRateManagerService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping
    fun getPaged(
        @RequestParam(required = false) ratePlanId: String?,
        pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {
        val page = interestRateManagerService.getPaged(ratePlanId?.toLong(),pageable)
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTOInterestRate>(it) }).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtoInterestRate: DTOInterestRate): ResponseEntity<DTOResponseSuccess<DTOInterestRate>> {
        return DTOResponseSuccess(interestRateManagerService.addOne(dtoInterestRate)).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoInterestRate: DTOInterestRate): ResponseEntity<DTOResponseSuccess<DTOInterestRate>> {
        return DTOResponseSuccess(interestRateManagerService.updateOne(id,dtoInterestRate)).response()
    }

    @DeleteMapping("{id}/{ratePlanId}")
    fun deleteOne(@PathVariable(name = "id") id: String,@PathVariable(name = "ratePlanId") ratePlanId: String): ResponseEntity<DTOResponseSuccess<DTOInterestRate>> {
        return DTOResponseSuccess(interestRateManagerService.deleteOne(id.toLong(),ratePlanId.toLong())).response()
    }

    @GetMapping("all")
    fun getInvokeAll( @RequestParam(required = false) ratePlanId: String): List<DTOInterestRate> {
        val page = interestRateManagerService.getPaged(ratePlanId.toLong(), Pageable.unpaged())
        return page.content.map { objectMapper.convertValue(it) }
    }



}