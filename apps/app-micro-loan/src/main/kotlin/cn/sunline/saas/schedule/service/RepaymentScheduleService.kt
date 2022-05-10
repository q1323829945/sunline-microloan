package cn.sunline.saas.repayment.schedule.service

import cn.sunline.saas.consumer.loan.dto.DTOLoanApplication
import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.interest.util.InterestRateUtil
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.product.invoke.ProductInvoke
import cn.sunline.saas.product.service.ProductService
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.factory.RepaymentScheduleCalcGeneration
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.dto.*
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.schedule.invoke.ScheduleInvoke
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.Instant
import org.joda.time.Period
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.criteria.Predicate


@Service
class RepaymentScheduleService(
    private val productService: ProductService,
    private val scheduleInvoke: ScheduleInvoke
){

    @Autowired
    private lateinit var repaymentScheduleCalcGeneration: RepaymentScheduleCalcGeneration

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun calculate(productId: Long, amount: BigDecimal, term: LoanTermType): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleTrialView>> {
        val loanDateInstant = Instant.now()

        val product = productService.findById(productId)

        var interestRate: BigDecimal = BigDecimal.ZERO
        if(product.interestFeature?.interestType == InterestType.FIXED) {
            val ratePlanId = product.interestFeature?.ratePlanId
            val ratePlan = scheduleInvoke.getRatePlan(ratePlanId!!.toLong())?.data
            val rates = objectMapper.convertValue<MutableList<InterestRate>>(ratePlan?.rates!!)
            interestRate = InterestRateUtil.getRate(term, rates)
        }else if(product.interestFeature?.interestType == InterestType.FLOATING_RATE_NOTE) {
            val ratePlanId = product.interestFeature?.ratePlanId
            val ratePlan = scheduleInvoke.getRatePlan(ratePlanId!!.toLong())?.data
            val rates = objectMapper.convertValue<MutableList<InterestRate>>(ratePlan?.rates!!)
            val baseRatePlan = scheduleInvoke.getRatePlanByRatePlanType(RatePlanType.STANDARD)?.data
            val baseRates = objectMapper.convertValue<MutableList<InterestRate>>(baseRatePlan?.rates!!)

            val floatInterestRate = InterestRateUtil.getRate(term, rates)
            val baseInterestRate = InterestRateUtil.getRate(term, baseRates)
            interestRate = InterestRateUtil.calRate(baseInterestRate,floatInterestRate)
        }else{
            interestRate = BigDecimal.ZERO
        }

        val repaymentDayType = product.repaymentFeature?.repaymentDayType ?: RepaymentDayType.BASE_LOAN_DAY
        var repaymentDay = 21;
        if (RepaymentDayType.BASE_LOAN_DAY == repaymentDayType) {
            repaymentDay = loanDateInstant.toDateTime().dayOfMonth().get()
        }

        val dtoRepaymentScheduleTrialView = repaymentScheduleCalcGeneration.calculator(
            DTORepaymentScheduleCalculateTrial(
                amount = amount,
                term = term,
                interestRate = interestRate,
                paymentMethod = product.repaymentFeature?.paymentMethod!!,
                startDate = loanDateInstant,
                endDate = term.calDays(loanDateInstant),
                repaymentFrequency = product.repaymentFeature?.frequency!!,
                baseYearDays = product.interestFeature?.baseYearDays!!,
                repaymentDay = repaymentDay,
                repaymentDayType = repaymentDayType
            )
        )
        return DTOResponseSuccess(dtoRepaymentScheduleTrialView).response()
    }

    fun register(dtoRepaymentSchedule: DTORepaymentScheduleAdd): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>> {
        //val savedRepaymentSchedule :RepaymentSchedule //= repaymentScheduleService.register(dtoRepaymentSchedule)
        val dtoRepaymentScheduleView = changeMapper(null)
        return DTOResponseSuccess(dtoRepaymentScheduleView).response()
    }


    private fun changeMapper(repaymentSchedule: RepaymentSchedule?): DTORepaymentScheduleView {
        val dtoRepaymentScheduleDetailView: MutableList<DTORepaymentScheduleDetailView> = ArrayList()
        for (schedule in repaymentSchedule?.schedule!!) {
            dtoRepaymentScheduleDetailView += DTORepaymentScheduleDetailView(
                id = schedule.id,
                repaymentScheduleId = repaymentSchedule.id,
                period = schedule.period,
                installment = schedule.installment,
                principal = schedule.principal,
                interest = schedule.interest,
                repaymentDate = CalcDateComponent.formatInstantToView(schedule.repaymentDate)
            )
        }
        return DTORepaymentScheduleView(
            repaymentScheduleId = repaymentSchedule.id,
            installment = repaymentSchedule.installment,
            interestRate = repaymentSchedule.interestRate,
            schedule = dtoRepaymentScheduleDetailView
        )
    }
}