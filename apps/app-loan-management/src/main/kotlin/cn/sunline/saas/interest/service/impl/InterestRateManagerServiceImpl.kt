package cn.sunline.saas.interest.service.impl

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.interest.dto.DTOInterestRateAdd
import cn.sunline.saas.interest.dto.DTOInterestRateChange
import cn.sunline.saas.interest.dto.DTOInterestRateView
import cn.sunline.saas.interest.exception.InterestRateBusinessException
import cn.sunline.saas.interest.exception.InterestRateNotFoundException
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.service.InterestRateManagerService
import cn.sunline.saas.interest.service.InterestRateService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate


@Service
class InterestRateManagerServiceImpl : InterestRateManagerService {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var interestRateService: InterestRateService

    override fun getPaged(ratePlanId: Long?, pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val page = interestRateService.getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("ratePlanId"), ratePlanId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable)
        return DTOPagedResponseSuccess(page.map { objectMapper.convertValue<DTOInterestRateView>(it) }).response()
    }

    override fun addOne(dtoInterestRate: DTOInterestRateAdd): ResponseEntity<DTOResponseSuccess<DTOInterestRateView>> {
        val interestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)
        val typeInterestRate = interestRateService.findByRatePlanIdAndPeriod(interestRate.ratePlanId,interestRate.period)
        if(typeInterestRate != null){
            throw InterestRateBusinessException("The type of interest has exist",ManagementExceptionCode.DATA_ALREADY_EXIST)
        }
        val savedInterestRate = interestRateService.addOne(interestRate)
        val responseInterestRate = objectMapper.convertValue<DTOInterestRateView>(savedInterestRate)
        return DTOResponseSuccess(responseInterestRate).response()
    }

    override fun updateOne(id: Long, dtoInterestRate: DTOInterestRateChange): ResponseEntity<DTOResponseSuccess<DTOInterestRateView>> {
        val oldInterestRate = interestRateService.getOne(id)?: throw InterestRateNotFoundException("Invalid interestRate",ManagementExceptionCode.DATA_NOT_FOUND)
        val newInterestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)
        val savedInterestRate = interestRateService.updateOne(oldInterestRate, newInterestRate)
        val responseInterestRate = objectMapper.convertValue<DTOInterestRateView>(savedInterestRate)
        return DTOResponseSuccess(responseInterestRate).response()
    }

    override fun deleteOne(id: Long): ResponseEntity<DTOResponseSuccess<DTOInterestRateView>> {
        val interestRate = interestRateService.getOne(id)?: throw InterestRateNotFoundException("Invalid interestRate",ManagementExceptionCode.DATA_NOT_FOUND)
        interestRateService.deleteById(id)
        val responseInterestRate = objectMapper.convertValue<DTOInterestRateView>(interestRate)
        return DTOResponseSuccess(responseInterestRate).response()
    }
}