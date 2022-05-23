package cn.sunline.saas.schedule.service

import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.interest.component.InterestRateHelper
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.product.service.ProductService
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.factory.RepaymentScheduleCalcGeneration
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.dto.*
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.rpc.invoke.ScheduleInvoke
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.Instant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class RepaymentScheduleService1(
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
            interestRate = InterestRateHelper.getRate(term, rates)!!
        }else if(product.interestFeature?.interestType == InterestType.FLOATING_RATE_NOTE) {
            val ratePlanId = product.interestFeature?.ratePlanId
            val ratePlan = scheduleInvoke.getRatePlan(ratePlanId!!.toLong())?.data
            val rates = objectMapper.convertValue<MutableList<InterestRate>>(ratePlan?.rates!!)
            val baseRatePlan = scheduleInvoke.getRatePlanByRatePlanType(RatePlanType.STANDARD)?.data
            val baseRates = objectMapper.convertValue<MutableList<InterestRate>>(baseRatePlan?.rates!!)

            val floatInterestRate = InterestRateHelper.getRate(term, rates)!!
            val baseInterestRate = InterestRateHelper.getRate(term, baseRates)!!
            interestRate = CalculateInterestRate(baseInterestRate).calRate(floatInterestRate)
        }else{
            interestRate = BigDecimal.ZERO
        }

        val repaymentDayType = product.repaymentFeature?.payment?.repaymentDayType ?: RepaymentDayType.BASE_LOAN_DAY
        var repaymentDay = 21;
        if (RepaymentDayType.BASE_LOAN_DAY == repaymentDayType) {
            repaymentDay = loanDateInstant.toDateTime().dayOfMonth().get()
        }

        val dtoRepaymentScheduleTrialView = repaymentScheduleCalcGeneration.calculator(
            DTORepaymentScheduleCalculateTrial(
                amount = amount,
                term = term,
                interestRate = interestRate,
                paymentMethod = product.repaymentFeature?.payment?.paymentMethod!!,
                startDate = loanDateInstant,
                endDate = term.term.calDate(loanDateInstant),
                repaymentFrequency = product.repaymentFeature?.payment?.frequency!!,
                baseYearDays = product.interestFeature?.interest?.baseYearDays!!,
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