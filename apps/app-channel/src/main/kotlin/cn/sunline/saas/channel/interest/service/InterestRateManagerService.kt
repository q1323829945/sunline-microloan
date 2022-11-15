package cn.sunline.saas.channel.interest.service

import cn.sunline.saas.channel.interest.model.InterestRate
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.interest.controller.dto.DTOInterestRate
import cn.sunline.saas.interest.exception.InterestRateBusinessException
import cn.sunline.saas.interest.exception.InterestRateNotFoundException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate


@Service
class InterestRateManagerService {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var interestRateService: InterestRateService

    @Autowired
    private lateinit var interestFeatureService: InterestFeatureService

    fun getPaged(ratePlanId: Long?, pageable: Pageable): Page<InterestRate> {
        return interestRateService.getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("ratePlanId"), ratePlanId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable)
    }

    fun addOne(dtoInterestRate: DTOInterestRate): DTOInterestRate {
        val interestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)
        checkRatePlanUseStatus(dtoInterestRate.ratePlanId.toLong())
        val typeInterestRate =
            interestRateService.findByRatePlanIdAndPeriod(interestRate.ratePlanId, interestRate.period)
        if (typeInterestRate != null) {
            throw InterestRateBusinessException( "The type of interest has exist")
        }
        val savedInterestRate = interestRateService.addOne(interestRate)
        return objectMapper.convertValue(savedInterestRate)
    }

    fun updateOne(id: Long, dtoInterestRate: DTOInterestRate): DTOInterestRate {
        val oldInterestRate = interestRateService.getOne(id) ?: throw InterestRateNotFoundException("Invalid interestRate")
        val newInterestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)
        checkRatePlanUseStatus(newInterestRate.ratePlanId)
        val savedInterestRate = interestRateService.updateOne(oldInterestRate, newInterestRate)
        return objectMapper.convertValue(savedInterestRate)
    }

    fun deleteOne(id: Long, ratePlanId: Long): DTOInterestRate {
        checkRatePlanUseStatus(ratePlanId)
        val interestRate = interestRateService.getOne(id) ?: throw InterestRateNotFoundException("Invalid interestRate")
        interestRateService.deleteById(id)
        return objectMapper.convertValue(interestRate)
    }

    private fun checkRatePlanUseStatus(ratePlanId: Long) {
        val interestFeatures = interestFeatureService.findByRatePlanId(ratePlanId)
        if (interestFeatures != null && interestFeatures.size > 0) {
            throw InterestRateBusinessException(
                "the rate plan has been used",
                ManagementExceptionCode.RATE_PLAN_HAS_BEEN_USED
            )
        }
    }
}