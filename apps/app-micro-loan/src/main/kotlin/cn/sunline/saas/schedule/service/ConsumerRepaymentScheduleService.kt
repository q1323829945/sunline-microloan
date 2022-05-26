package cn.sunline.saas.schedule.service

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.interest.component.InterestRateHelper
import cn.sunline.saas.interest.service.RatePlanService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.repayment.schedule.model.dto.*
import cn.sunline.saas.rpc.invoke.impl.ProductInvokeImpl
import cn.sunline.saas.schedule.Schedule
import cn.sunline.saas.schedule.ScheduleService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.Instant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ConsumerRepaymentScheduleService(private val tenantDateTime: TenantDateTime) {

    @Autowired
    private lateinit var productInvokeImpl: ProductInvokeImpl

    @Autowired
    private lateinit var ratePlanService: RatePlanService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun initiate() {

    }

    fun calculate(productId: Long, amount: BigDecimal, term: LoanTermType): MutableList<Schedule> {

        val loanProduct = productInvokeImpl.getProductInfoByProductId(productId)?.data ?: throw Exception("error")

        val ratePlanId = loanProduct.interestFeature.ratePlanId
        val ratePlan = ratePlanService.getOne(ratePlanId.toLong())!!
        val rates = ratePlan.rates
        val interestRate = InterestRateHelper.getRate(term, rates)!!
        val repaymentFeature = loanProduct.repaymentFeature
        val interestFeature = loanProduct.interestFeature.interest
        val baseYearDays = interestFeature.baseYearDays

        return ScheduleService(
            amount,
            interestRate,
            term,
            repaymentFeature.payment.frequency,
            tenantDateTime.now().toInstant(),
            null,
            baseYearDays
        ).getSchedules(repaymentFeature.payment.paymentMethod)
    }
}