package cn.sunline.saas.schedule.service

import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.interest.arrangement.exception.BaseRateNullException
import cn.sunline.saas.interest.component.InterestRateHelper
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.interest.service.RatePlanService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.repayment.schedule.model.dto.*
import cn.sunline.saas.rpc.invoke.dto.DTOInvokeRates
import cn.sunline.saas.rpc.invoke.impl.ProductInvokeImpl
import cn.sunline.saas.rpc.invoke.impl.RatePlanInvokeImpl
import cn.sunline.saas.schedule.Schedule
import cn.sunline.saas.schedule.ScheduleService
import cn.sunline.saas.schedule.dto.DTORatesView
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonSerializable
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.DateTime
import org.joda.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ConsumerRepaymentScheduleService(private val tenantDateTime: TenantDateTime) {

    private val logger: Logger = LoggerFactory.getLogger(ConsumerRepaymentScheduleService::class.java)

    @Autowired
    private lateinit var productInvokeImpl: ProductInvokeImpl

    @Autowired
    private lateinit var ratePlanInvokeImpl: RatePlanInvokeImpl

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun initiate() {

    }

    fun calculate(productId: Long, amount: BigDecimal, term: LoanTermType): MutableList<Schedule> {

        val loanProduct = productInvokeImpl.getProductInfoByProductId(productId) ?: throw Exception("error")
        val ratePlanId = loanProduct.interestFeature.ratePlanId
        val interestRate = getExecutionRate(loanProduct.interestFeature.interestType,term,ratePlanId.toLong())
        return ScheduleService(
            amount,
            interestRate,
            term,
            loanProduct.repaymentFeature.payment.frequency,
            tenantDateTime.now().toInstant(),
            //DateTime.now().toInstant(),
            null,
            loanProduct.interestFeature.interest.baseYearDays
        ).getSchedules(loanProduct.repaymentFeature.payment.paymentMethod)
    }


    private fun changeRate(list: List<DTOInvokeRates>, ratePlanId: Long): MutableList<InterestRate> {
        val rates = mutableListOf<InterestRate>()
        for (rate in list) {
            rates.add(
                InterestRate(
                    rate.id.toLong(),
                    rate.period,
                    rate.rate.toBigDecimal(),
                    ratePlanId
                )
            )
        }
        return rates
    }

    private fun getExecutionRate(interestType: InterestType, term: LoanTermType, ratePlanId: Long): BigDecimal {

        val rateResult = ratePlanInvokeImpl.getRatePlanByRatePlanId(ratePlanId.toLong())
        val ratesModel = changeRate(rateResult.rates, ratePlanId.toLong())
        val rate = InterestRateHelper.getRate(term, ratesModel)!!
        logger.debug("rate:"+ rate.toString())
        val executionRate = when (interestType) {
            InterestType.FIXED -> rate
            InterestType.FLOATING_RATE_NOTE ->{
                val baseRateResult = ratePlanInvokeImpl.getRatePlanByType(RatePlanType.STANDARD)
                val baseRateModel = changeRate(baseRateResult.rates, ratePlanId.toLong())
                val baseRate = InterestRateHelper.getRate(term, baseRateModel)
                logger.debug("baseRate:"+ baseRate.toString())
                if (baseRate == null) {
                    throw BaseRateNullException("base rate must be not null when interest type is floating rate")
                } else {
                    CalculateInterestRate(baseRate).calRateWithNoPercent(rate)
                }
            }
        }
        logger.debug("executionRate:"+ executionRate.toString())
        return executionRate
    }
}