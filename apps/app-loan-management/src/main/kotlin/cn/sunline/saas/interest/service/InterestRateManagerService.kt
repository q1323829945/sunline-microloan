package cn.sunline.saas.interest.service

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.LoanAmountTierType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.controller.dto.DTOInterestRate
import cn.sunline.saas.interest.controller.dto.DTOInterestRateView
import cn.sunline.saas.interest.controller.dto.DTORatePlan
import cn.sunline.saas.interest.exception.InterestRateBusinessException
import cn.sunline.saas.interest.exception.InterestRateNotFoundException
import cn.sunline.saas.interest.exception.RatePlanNotFoundException
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.rpc.invoke.impl.PageInvokeImpl
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
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

    fun getPaged(ratePlanId: Long?, pageable: Pageable): Page<DTOInterestRateView> {

        val interestRates = interestRateService.getPage(ratePlanId, pageable).content
        val rateViewList = reBuildPeriod(ratePlanId!!, interestRates)
        return PageInvokeImpl<DTOInterestRateView>().rePaged(rateViewList, pageable)
    }

    fun addOne(dtoInterestRate: DTOInterestRate): DTOInterestRate {
        val interestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)

        checkPackage(
            dtoInterestRate.ratePlanId.toLong(),
            dtoInterestRate.toPeriod,
            dtoInterestRate.toAmountPeriod
        )
        val savedInterestRate = interestRateService.addOne(interestRate)
        return objectMapper.convertValue(savedInterestRate)
    }

    private fun reBuildPeriod(ratePlanId: Long, interestRates: List<InterestRate>): List<DTOInterestRateView> {
        val ratePlan = ratePlanService.getOne(ratePlanId)
            ?: throw RatePlanNotFoundException("invalid rate plan", ManagementExceptionCode.DATA_NOT_FOUND)
        val rates = ArrayList<DTOInterestRateView>()
        when (ratePlan.type) {
            RatePlanType.LOAN_AMOUNT_TIER_CUSTOMER -> {
                var fromAmountPeriod = BigDecimal.ZERO
                interestRates.sortedBy { it.toAmountPeriod }.forEach {
                    rates += DTOInterestRateView(
                        id = it.id.toString(),
                        fromAmountPeriod = fromAmountPeriod,
                        toAmountPeriod = it.toAmountPeriod,
                        ratePlanId = it.ratePlanId.toString(),
                        rate = it.rate.toPlainString()
                    )
                    fromAmountPeriod = it.toAmountPeriod!!
                }
            }

            RatePlanType.LOAN_TERM_TIER_CUSTOMER -> {
                var fromPeriod = LoanTermType.ONE_MONTH
                interestRates.sortedBy { it.toPeriod!!.term.toMonthUnit().num }.forEach {
                    rates += DTOInterestRateView(
                        id = it.id.toString(),
                        fromPeriod = fromPeriod,
                        toPeriod = it.toPeriod,
                        ratePlanId = it.ratePlanId.toString(),
                        rate = it.rate.toPlainString()
                    )
                    fromPeriod = it.toPeriod!!
                }
            }

            RatePlanType.CUSTOMER,
            RatePlanType.STANDARD -> {
                interestRates.forEach {
                    rates += DTOInterestRateView(
                        id = it.id.toString(),
                        fromPeriod = it.toPeriod,
                        toPeriod = it.toPeriod,
                        ratePlanId = it.ratePlanId.toString(),
                        rate = it.rate.toPlainString()
                    )
                }
            }
        }
        return rates
    }

    fun updateOne(id: Long, dtoInterestRate: DTOInterestRate): DTOInterestRate {
        val interestRate = interestRateService.getOne(id) ?: throw InterestRateNotFoundException(
            "invalid interest rate",
            ManagementExceptionCode.INTEREST_RATE_NOT_FOUND
        )
        checkPackage(
            id,
            dtoInterestRate.toPeriod,
            dtoInterestRate.toAmountPeriod
        )
        val newInterestRate = objectMapper.convertValue<InterestRate>(dtoInterestRate)
        val savedInterestRate = interestRateService.updateOne(interestRate,newInterestRate)
        return objectMapper.convertValue(savedInterestRate)
    }

    private fun checkPackage(
        ratePlanId: Long,
        toPeriod: LoanTermType?,
        toAmountPeriod: BigDecimal?
    ) {
        checkPeriod(
            ratePlanId,
            toPeriod,
            toAmountPeriod
        )
        checkRatePlanUseStatus(ratePlanId)
        checkInterestRateExist(
            ratePlanId,
            toPeriod,
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
        toPeriod: LoanTermType?,
        toAmountPeriod: BigDecimal?
    ) {
        val typeInterestRate =
            interestRateService.findByRatePlanIdAndPeriod(
                ratePlanId,
                toPeriod,
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
        toPeriod: LoanTermType?,
        toAmountPeriod: BigDecimal?
    ) {
        val ratePlan = ratePlanService.getOne(ratePlanId) ?: throw InterestRateBusinessException(
            "invalid rate plan",
            ManagementExceptionCode.RATE_PLAN_NOT_FOUND
        )
        when (ratePlan.type) {
            RatePlanType.LOAN_AMOUNT_TIER_CUSTOMER -> {
                toAmountPeriod ?: throw InterestRateBusinessException(
                    "the ToAmountPeriod is not null",
                    ManagementExceptionCode.INTEREST_RATE_PERIOD_IS_NOT_NULL
                )
            }

            RatePlanType.LOAN_TERM_TIER_CUSTOMER -> {
                toPeriod ?: throw InterestRateBusinessException(
                    "the toPeriod is not null",
                    ManagementExceptionCode.INTEREST_RATE_PERIOD_IS_NOT_NULL
                )
            }

            RatePlanType.STANDARD,
            RatePlanType.CUSTOMER -> {
                toPeriod ?: throw InterestRateBusinessException(
                    "the period is not null",
                    ManagementExceptionCode.INTEREST_RATE_PERIOD_IS_NOT_NULL
                )
            }
        }
    }
}