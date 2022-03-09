package cn.sunline.saas.controllers.interest

import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.RatePlan
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.interest.service.InterestRateService
import cn.sunline.saas.interest.service.RatePlanService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("interestRate")
class InterestRateController {

    data class DTOInterestRateAdd(
            val period: String,
            val rate: BigDecimal,
            val ratePlanId:Long
    )

    data class DTOInterestRateView(
            val id: Long,
            val period: String,
            val rate: BigDecimal,
            val ratePlanId:Long
    )

    data class DTOInterestRateChange(
            val period: String,
            val rate: BigDecimal
    )


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @Autowired
    private lateinit var interestRateService: InterestRateService

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<Any> {
        return ResponseEntity.ok(interestRateService.getPaged(pageable = pageable).map { objectMapper.convertValue<DTOInterestRateView>(it) })
    }


    @PostMapping
    fun addOne(@RequestBody dtoRatePlan: DTOInterestRateAdd): ResponseEntity<DTOInterestRateView> {
        val ratePlan = objectMapper.convertValue<InterestRate>(dtoRatePlan)
        val savedRatePlan = interestRateService.save(ratePlan)
        val responseRatePlan = objectMapper.convertValue<DTOInterestRateView>(savedRatePlan)
        return ResponseEntity.ok(responseRatePlan)
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoRatePlan: DTOInterestRateChange): ResponseEntity<DTOInterestRateView> {
        val oldRatePlan = interestRateService.getOne(id)?: throw Exception("Invalid role")
        val newRatePlan = objectMapper.convertValue<InterestRate>(dtoRatePlan)

        val savedRatePlan = interestRateService.updateOne(newRatePlan, oldRatePlan)
        val responseRatePlan = objectMapper.convertValue<DTOInterestRateView>(savedRatePlan)
        return ResponseEntity.ok(responseRatePlan)
    }
}