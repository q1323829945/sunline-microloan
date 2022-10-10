package cn.sunline.saas.interest.service

import cn.sunline.saas.channel.interest.service.InterestFeatureService
import cn.sunline.saas.channel.interest.service.InterestRateService
import cn.sunline.saas.channel.interest.service.RatePlanService
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.LoanAmountTierType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.controller.dto.DTOInterestRate
import cn.sunline.saas.interest.exception.InterestRateBusinessException
import cn.sunline.saas.interest.exception.InterestRateNotFoundException
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.RatePlanType
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

    @Autowired
    private lateinit var ratePlanService: RatePlanService

    fun getPaged(ratePlanId: Long?, pageable: Pageable): Page<InterestRate> {
        return interestRateService.getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("ratePlanId"), ratePlanId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, pageable)
    }

    fun addOne(dtoInterestRate: DTOInterestRate): DTOInterestRate {
        val interestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)
        interestRate.toPeriod = dtoInterestRate.toPeriod ?: dtoInterestRate.fromPeriod
        interestRate.toAmountPeriod = dtoInterestRate.toAmountPeriod ?: dtoInterestRate.fromAmountPeriod
        checkRatePlanUseStatus(dtoInterestRate.ratePlanId.toLong())
        checkInterestRateExist(
            interestRate.ratePlanId,
            interestRate.fromPeriod,
            interestRate.toPeriod,
            interestRate.fromAmountPeriod,
            interestRate.toAmountPeriod
        )
        val savedInterestRate = interestRateService.addOne(interestRate)
        return objectMapper.convertValue(savedInterestRate)
    }

    fun updateOne(id: Long, dtoInterestRate: DTOInterestRate): DTOInterestRate {
        val oldInterestRate = interestRateService.getOne(id) ?: throw InterestRateNotFoundException(
            "Invalid interestRate",
            ManagementExceptionCode.DATA_NOT_FOUND
        )
        val newInterestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)
        checkRatePlanUseStatus(newInterestRate.ratePlanId)
        checkInterestRateExist(
            newInterestRate.ratePlanId,
            newInterestRate.fromPeriod,
            newInterestRate.toPeriod,
            newInterestRate.fromAmountPeriod,
            newInterestRate.toAmountPeriod
        )
        val savedInterestRate = interestRateService.updateOne(oldInterestRate, newInterestRate)
        return objectMapper.convertValue(savedInterestRate)
    }

    fun deleteOne(id: Long, ratePlanId: Long): DTOInterestRate {
        checkRatePlanUseStatus(ratePlanId)
        val interestRate = interestRateService.getOne(id) ?: throw InterestRateNotFoundException(
            "Invalid interestRate",
            ManagementExceptionCode.DATA_NOT_FOUND
        )
        interestRateService.deleteById(id)
        return objectMapper.convertValue(interestRate)
    }

    private fun checkRatePlanUseStatus(ratePlanId: Long) {
        val interestFeatures = interestFeatureService.findByRatePlanId(ratePlanId)
        if (!interestFeatures.isNullOrEmpty()) {
            throw InterestRateBusinessException(
                "This rate plan has been used",
                ManagementExceptionCode.RATE_PLAN_HAS_BEEN_USED
            )
        }
    }

    private fun checkInterestRateExist(
        ratePlanId: Long,
        fromPeriod: LoanTermType?,
        toPeriod: LoanTermType?,
        fromAmountPeriod: LoanAmountTierType?,
        toAmountPeriod: LoanAmountTierType?
    ) {
        val ratePlan = ratePlanService.getOne(ratePlanId) ?: throw InterestRateNotFoundException(
            "Invalid rate plan",
            ManagementExceptionCode.DATA_NOT_FOUND
        )
        val typeInterestRate =
            interestRateService.findByRatePlanIdAndPeriod(
                ratePlanId,
                fromPeriod,
                if (RatePlanType.STANDARD == ratePlan.type || RatePlanType.CUSTOMER == ratePlan.type) fromPeriod else toPeriod,
                fromAmountPeriod,
                toAmountPeriod
            )
        if (typeInterestRate != null) {
            throw InterestRateBusinessException(
                "The type of interest has exist",
                ManagementExceptionCode.DATA_ALREADY_EXIST
            )
        }
    }
}