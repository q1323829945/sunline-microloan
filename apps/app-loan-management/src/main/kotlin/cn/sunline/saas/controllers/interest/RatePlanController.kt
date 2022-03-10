package cn.sunline.saas.controllers.interest

import cn.sunline.saas.interest.model.RatePlan
import cn.sunline.saas.interest.model.RatePlanType
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
import java.math.BigDecimal
import cn.sunline.saas.seq.Sequence
import org.springframework.data.domain.Page

@RestController
@RequestMapping("RatePlan")
class RatePlanController {

    data class DTORatesView(val id:Long,val period: String,val rate: BigDecimal)

    data class DTORatePlanAdd(
            val name: String,
            val type: RatePlanType
    )

    data class DTORatePlanView(
            val id: Long,
            val name: String,
            val type: RatePlanType,
            val rates: List<DTORatesView>
    )

    data class DTORatePlanChange(
            val id: Long,
            val name: String,
            val type: RatePlanType
    )


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var ratePlanService:RatePlanService


    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = ratePlanService.getPaged(pageable = pageable)
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTORatePlanView>(it) }).response()
    }


    @PostMapping
    fun addOne(@RequestBody dtoRatePlan: DTORatePlanAdd): ResponseEntity<DTOResponseSuccess<DTORatePlanView>> {
        val ratePlan = objectMapper.convertValue<RatePlan>(dtoRatePlan)
        val savedRatePlan = ratePlanService.addOne(ratePlan)
        val responseRatePlan = objectMapper.convertValue<DTORatePlanView>(savedRatePlan)
        return DTOResponseSuccess(responseRatePlan).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoRatePlan: DTORatePlanChange): ResponseEntity<DTOResponseSuccess<DTORatePlanView>> {
        val oldRatePlan = ratePlanService.getOne(id)?: throw Exception("Invalid ratePlan")
        val newRatePlan = objectMapper.convertValue<RatePlan>(dtoRatePlan)
        val savedRatePlan = ratePlanService.updateOne(oldRatePlan, newRatePlan)
        val responseRatePlan = objectMapper.convertValue<DTORatePlanView>(savedRatePlan)
        return DTOResponseSuccess(responseRatePlan).response()
    }

}