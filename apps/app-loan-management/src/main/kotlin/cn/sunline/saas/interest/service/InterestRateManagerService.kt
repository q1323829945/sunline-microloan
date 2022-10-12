package cn.sunline.saas.interest.service

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
        return interestRateService.getPage(ratePlanId, pageable)
    }

    fun addOne(dtoInterestRate: DTOInterestRate): DTOInterestRate {
        val interestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)
        checkPackage(
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
            "invalid interest rate",
            ManagementExceptionCode.INTEREST_RATE_NOT_FOUND
        )
        val newInterestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)
        checkPackage(
            newInterestRate.ratePlanId,
            newInterestRate.fromPeriod,
            newInterestRate.toPeriod,
            newInterestRate.fromAmountPeriod,
            newInterestRate.toAmountPeriod
        )
        val savedInterestRate = interestRateService.updateOne(oldInterestRate, newInterestRate)
        return objectMapper.convertValue(savedInterestRate)
    }

    private fun checkPackage(
        ratePlanId: Long,
        fromPeriod: LoanTermType?,
        toPeriod: LoanTermType?,
        fromAmountPeriod: LoanAmountTierType?,
        toAmountPeriod: LoanAmountTierType?
    ) {
        checkPeriod(
            ratePlanId,
            fromPeriod,
            toPeriod,
            fromAmountPeriod,
            toAmountPeriod
        )
        checkRatePlanUseStatus(ratePlanId)
        checkInterestRateExist(
            ratePlanId,
            fromPeriod,
            toPeriod,
            fromAmountPeriod,
            toAmountPeriod
        )
    }

    fun deleteOne(id: Long, ratePlanId: Long): DTOInterestRate {
        checkRatePlanUseStatus(ratePlanId)
        val interestRate = interestRateService.getOne(id) ?: throw InterestRateNotFoundException(
            "invalid interest rate",
            ManagementExceptionCode.INTEREST_RATE_NOT_FOUND
        )
        interestRateService.deleteById(id)
        return objectMapper.convertValue(interestRate)
    }

    private fun checkRatePlanUseStatus(ratePlanId: Long) {
        val interestFeatures = interestFeatureService.findByRatePlanId(ratePlanId)
        if (!interestFeatures.isNullOrEmpty()) {
            throw InterestRateBusinessException(
                "this rate plan has been used",
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
        val typeInterestRate =
            interestRateService.findByRatePlanIdAndPeriod(
                ratePlanId,
                fromPeriod,
                toPeriod,
                fromAmountPeriod,
                toAmountPeriod
            )
        if (typeInterestRate != null) {
            throw InterestRateBusinessException(
                "the type of interest already exist",
                ManagementExceptionCode.DATA_ALREADY_EXIST
            )
        }
    }

    private fun checkPeriod(
        ratePlanId: Long,
        fromPeriod: LoanTermType?,
        toPeriod: LoanTermType?,
        fromAmountPeriod: LoanAmountTierType?,
        toAmountPeriod: LoanAmountTierType?
    ) {
        val ratePlan = ratePlanService.getOne(ratePlanId) ?: throw InterestRateBusinessException(
            "invalid rate plan",
            ManagementExceptionCode.RATE_PLAN_NOT_FOUND
        )
        when (ratePlan.type) {
            RatePlanType.LOAN_AMOUNT_TIER_CUSTOMER -> {
                fromAmountPeriod ?: throw InterestRateBusinessException(
                    "the fromAmountPeriod is not null",
                    ManagementExceptionCode.INTEREST_RATE_PERIOD_IS_NOT_NULL
                )
                toAmountPeriod ?: throw InterestRateBusinessException(
                    "the ToAmountPeriod is not null",
                    ManagementExceptionCode.INTEREST_RATE_PERIOD_IS_NOT_NULL
                )
                if (toAmountPeriod.amount < fromAmountPeriod.amount) {
                    throw InterestRateBusinessException(
                        "the fromAmountPeriod greater than toAmountPeriod",
                        ManagementExceptionCode.INTEREST_RATE_PERIOD_IS_NOT_CORRECT
                    )
                }
            }

            RatePlanType.LOAN_TERM_TIER_CUSTOMER -> {
                fromPeriod ?: throw InterestRateBusinessException(
                    "the fromPeriod is not null",
                    ManagementExceptionCode.INTEREST_RATE_PERIOD_IS_NOT_NULL
                )
                toPeriod ?: throw InterestRateBusinessException(
                    "the toPeriod is not null",
                    ManagementExceptionCode.INTEREST_RATE_PERIOD_IS_NOT_NULL
                )
                if (toPeriod.term < fromPeriod.term) {
                    throw InterestRateBusinessException(
                        "the fromPeriod greater than toPeriod",
                        ManagementExceptionCode.INTEREST_RATE_PERIOD_IS_NOT_CORRECT
                    )
                }
            }

            RatePlanType.STANDARD,
            RatePlanType.CUSTOMER -> {
                fromPeriod ?: throw InterestRateBusinessException(
                    "the period is not null",
                    ManagementExceptionCode.INTEREST_RATE_PERIOD_IS_NOT_NULL
                )
            }
        }
    }
}