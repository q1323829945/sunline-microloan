package cn.sunline.saas.schedule.service

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.interest.component.InterestRateHelper
import cn.sunline.saas.interest.service.InterestFeatureService
import cn.sunline.saas.interest.service.RatePlanService
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.factory.RepaymentScheduleCalcGenerationService
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.dto.*
import cn.sunline.saas.repayment.schedule.service.RepaymentScheduleService
import cn.sunline.saas.repayment.service.RepaymentFeatureService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.rpc.invoke.impl.ProductInvokeImpl
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.bytebuddy.implementation.bytecode.Throw
import org.joda.time.Instant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.criteria.Predicate

@Service
class ConsumerRepaymentScheduleService{

    @Autowired
    private lateinit var repaymentScheduleCalcGenerationService: RepaymentScheduleCalcGenerationService

    @Autowired
    private lateinit var productInvokeImpl: ProductInvokeImpl

    @Autowired
    private lateinit var ratePlanService: RatePlanService

    @Autowired
    private lateinit var repaymentFeatureService: RepaymentFeatureService


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun initiate() {

    }

    fun calculate(productId: Long, amount: BigDecimal, term: LoanTermType): DTORepaymentScheduleView {

        val loanDateInstant = Instant.now()
        val loanProduct = productInvokeImpl.getProductInfoByProductId(productId)?.data ?:throw Exception("error")



        val ratePlanId = (loanProduct.interestFeature.ratePlanId)
        val ratePlan = ratePlanService.getOne(ratePlanId.toLong())!!
        val rates = ratePlan.rates
        val interestRate = InterestRateHelper.getRate(term, rates)!!


        val repaymentFeature = loanProduct.repaymentFeature
        val repaymentDayType = repaymentFeature.payment.repaymentDayType ?: RepaymentDayType.BASE_LOAN_DAY
        var repaymentDay = 21;
        if (RepaymentDayType.BASE_LOAN_DAY == repaymentDayType) {
            repaymentDay = loanDateInstant.toDateTime().dayOfMonth().get()
        }

        val interestFeature = loanProduct.interestFeature.interest
        val baseYearDays = interestFeature.baseYearDays

        return repaymentScheduleCalcGenerationService.calculator(
            DTORepaymentScheduleCalculateTrial(
                amount = amount,
                term = term,
                interestRate = interestRate,
                paymentMethod = repaymentFeature.payment.paymentMethod,
                startDate = loanDateInstant,
                endDate = term.term.calDate(loanDateInstant),
                repaymentFrequency = repaymentFeature.payment.frequency,
                baseYearDays = baseYearDays,
                repaymentDay = repaymentDay,
                repaymentDayType = repaymentDayType
            )
        )
    }
}