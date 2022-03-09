package cn.sunline.saas.controllers.interest

import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.service.InterestRateService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import cn.sunline.saas.seq.Sequence

@RestController
@RequestMapping("interestRate")
class InterestRateController {

    data class DTOInterestRateAdd(
            var id: Long?,
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
            val id: Long,
            val period: String,
            val rate: BigDecimal
    )


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @Autowired
    private lateinit var interestRateService: InterestRateService

    @Autowired
    private lateinit var snowflakeService: Sequence

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<Any> {
        return ResponseEntity.ok(interestRateService.getPaged(pageable = pageable).map { objectMapper.convertValue<DTOInterestRateView>(it) })
    }


    @PostMapping
    fun addOne(@RequestBody dtoInterestRate: DTOInterestRateAdd): ResponseEntity<DTOInterestRateView> {
        dtoInterestRate.id = snowflakeService.nextId()
        val interestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)
        val savedInterestRate = interestRateService.save(interestRate)
        val responseInterestRate = objectMapper.convertValue<DTOInterestRateView>(savedInterestRate)
        return ResponseEntity.ok(responseInterestRate)
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoInterestRate: DTOInterestRateChange): ResponseEntity<DTOInterestRateView> {
        val oldInterestRate = interestRateService.getOne(id)?: throw Exception("Invalid interestRate")
        val newInterestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)

        val savedInterestRate = interestRateService.updateOne(newInterestRate, oldInterestRate)
        val responseInterestRate = objectMapper.convertValue<DTOInterestRateView>(savedInterestRate)
        return ResponseEntity.ok(responseInterestRate)
    }


    @DeleteMapping("{id}")
    fun deleteOne(@PathVariable id: Long): ResponseEntity<DTOInterestRateView> {
        val interestRate = interestRateService.getOne(id)?: throw Exception("Invalid interestRate")
        interestRateService.deleteById(id)
        val responseInterestRate = objectMapper.convertValue<DTOInterestRateView>(interestRate)
        return ResponseEntity.ok(responseInterestRate)
    }
}