package cn.sunline.saas.interest.controller

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.fee.model.dto.DTOFeeFeatureAdd
import cn.sunline.saas.interest.exception.InterestRateNotFoundException
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.dto.DTOInterestFeatureAdd
import cn.sunline.saas.interest.service.InterestRateService
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.dto.DTOAmountLoanProductConfiguration
import cn.sunline.saas.loan.product.model.dto.DTOTermLoanProductConfiguration
import cn.sunline.saas.repayment.model.dto.DTORepaymentFeatureAdd
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
@RequestMapping("InterestRate")
class InterestRateController {

    data class DTOInterestRateAdd(
            val period: String,
            val rate: String,
            val ratePlanId:Long
    )

    data class DTOInterestRateView(
            val id: Long,
            val period: String,
            val rate: String,
            val ratePlanId:Long
    )

    data class DTOInterestRateChange(
            val identificationCode: String,
            val name: String,
            val description: String,
            val loanProductType: LoanProductType,
            val loanPurpose: String,
            val amountConfiguration: DTOAmountLoanProductConfiguration?,
            val termConfiguration: DTOTermLoanProductConfiguration?,
            val interestFeature: DTOInterestFeatureAdd?,
            val repaymentFeature: DTORepaymentFeatureAdd?,
            val feeFeatures:MutableList<DTOFeeFeatureAdd>?
    )

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var interestRateService: InterestRateService

    @GetMapping
    fun getPaged(
        @RequestParam(required = false) ratePlanId: Long,
        pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {
        val page = interestRateService.getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("ratePlanId"), ratePlanId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable)
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTOInterestRateView>(it) }).response()
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
        val oldInterestRate = interestRateService.getOne(id)?: throw InterestRateNotFoundException("Invalid interestRate",ManagementExceptionCode.DATA_NOT_FOUND)
        val newInterestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)

        val savedInterestRate = interestRateService.updateOne(oldInterestRate, newInterestRate)
        val responseInterestRate = objectMapper.convertValue<DTOInterestRateView>(savedInterestRate)
        return DTOResponseSuccess(responseInterestRate).response()
    }

    @DeleteMapping("{id}")
    fun deleteOne(@PathVariable id: Long): ResponseEntity<DTOResponseSuccess<DTOInterestRateView>> {
        val interestRate = interestRateService.getOne(id)?: throw InterestRateNotFoundException("Invalid interestRate",ManagementExceptionCode.DATA_NOT_FOUND)
        interestRateService.deleteById(id)
        val responseInterestRate = objectMapper.convertValue<DTOInterestRateView>(interestRate)
        return DTOResponseSuccess(responseInterestRate).response()
    }
}