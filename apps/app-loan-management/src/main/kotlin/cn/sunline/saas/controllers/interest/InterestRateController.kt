package cn.sunline.saas.controllers.interest

import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.service.InterestRateService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import javax.persistence.criteria.Predicate

@RestController
@RequestMapping("InterestRate")
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
            val id: Long,
            val period: String,
            val rate: BigDecimal
    )


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @Autowired
    private lateinit var interestRateService: InterestRateService


    @GetMapping
    fun getPaged(@RequestParam(required = false)ratePlanId:Long,pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = interestRateService.getPaged({root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("ratePlanId"),ratePlanId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        },pageable)
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTOInterestRateView>(it)}).response()
    }


    @PostMapping
    fun addOne(@RequestBody dtoInterestRate: DTOInterestRateAdd): ResponseEntity<DTOResponseSuccess<DTOInterestRateView>> {
        val interestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)
        val savedInterestRate = interestRateService.addOne(interestRate)
        val responseInterestRate = objectMapper.convertValue<DTOInterestRateView>(savedInterestRate)
        return DTOResponseSuccess(responseInterestRate).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoInterestRate: DTOInterestRateChange): ResponseEntity<DTOResponseSuccess<DTOInterestRateView>> {
        val oldInterestRate = interestRateService.getOne(id)?: throw Exception("Invalid interestRate")
        val newInterestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)

        val savedInterestRate = interestRateService.updateOne(newInterestRate, oldInterestRate)
        val responseInterestRate = objectMapper.convertValue<DTOInterestRateView>(savedInterestRate)
        return DTOResponseSuccess(responseInterestRate).response()
    }


    @DeleteMapping("{id}")
    fun deleteOne(@PathVariable id: Long): ResponseEntity<DTOResponseSuccess<DTOInterestRateView>> {
        val interestRate = interestRateService.getOne(id)?: throw Exception("Invalid interestRate")
        interestRateService.deleteById(id)
        val responseInterestRate = objectMapper.convertValue<DTOInterestRateView>(interestRate)
        return DTOResponseSuccess(responseInterestRate).response()
    }
}