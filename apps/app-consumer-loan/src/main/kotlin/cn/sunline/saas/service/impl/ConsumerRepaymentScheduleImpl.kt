package cn.sunline.saas.service.impl

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.interest.constant.BaseYearDays
import cn.sunline.saas.interest.service.InterestFeatureService
import cn.sunline.saas.interest.service.RatePlanService
import cn.sunline.saas.interest.util.InterestRateUtil
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.factory.RepaymentScheduleCalcGeneration
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.dto.*
import cn.sunline.saas.repayment.schedule.service.RepaymentScheduleService
import cn.sunline.saas.repayment.service.RepaymentFeatureService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.service.ConsumerRepaymentScheduleService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.DateTime
import org.joda.time.Instant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.criteria.Predicate

@Service
class ConsumerRepaymentScheduleImpl : ConsumerRepaymentScheduleService {

    @Autowired
    private lateinit var repaymentScheduleCalcGeneration: RepaymentScheduleCalcGeneration

    @Autowired
    private lateinit var loanProductService: LoanProductService

    @Autowired
    private lateinit var interestProductFeatureService: InterestFeatureService

    @Autowired
    private lateinit var ratePlanService: RatePlanService

    @Autowired
    private lateinit var repaymentScheduleService: RepaymentScheduleService

    @Autowired
    private lateinit var repaymentFeatureService: RepaymentFeatureService


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun initiate() {

    }

    override fun calculate(productId: Long,amount: BigDecimal,term: LoanTermType): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleTrialView>> {
        val loanDateInstant = Instant.now()
        val interestProduct = interestProductFeatureService.findByProductId(productId)

        val ratePlanId = (interestProduct?.ratePlanId)!!
        val ratePlan = ratePlanService.getOne(ratePlanId)!!
        val rates = ratePlan.rates
        val interestRate = InterestRateUtil.getRate(term, rates)

        val repaymentFeature = repaymentFeatureService.getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("productId"), productId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.ofSize(1)).firstOrNull()

        val repaymentDayType = repaymentFeature?.payment?.repaymentDayType ?: RepaymentDayType.BASE_LOAN_DAY
        var repaymentDay = 21;
        if (RepaymentDayType.BASE_LOAN_DAY == repaymentDayType) {
            repaymentDay = loanDateInstant.toDateTime().dayOfMonth().get()
        }

        val dtoRepaymentScheduleTrialView = repaymentScheduleCalcGeneration.calculator(
            DTORepaymentScheduleCalculateTrial(
                amount = amount,
                term = term,
                interestRate = interestRate,
                paymentMethod = repaymentFeature?.payment?.paymentMethod!!,
                startDate = loanDateInstant,
                endDate = term.calDays(loanDateInstant),
                repaymentFrequency = repaymentFeature.payment.frequency,
                baseYearDays = interestProduct.interest.baseYearDays,
                repaymentDay = repaymentDay,
                repaymentDayType = repaymentDayType
            )
        )
        return DTOResponseSuccess(dtoRepaymentScheduleTrialView).response()
    }

    override fun register(dtoRepaymentSchedule: DTORepaymentScheduleAdd): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>> {
        val savedRepaymentSchedule = repaymentScheduleService.register(dtoRepaymentSchedule)
        val dtoRepaymentScheduleView = changeMapper(savedRepaymentSchedule)
        return DTOResponseSuccess(dtoRepaymentScheduleView).response()
    }

    override fun updateOne(id: Long, productId: Long, repaymentDate: String, term: LoanTermType, remainLoanAmount: BigDecimal): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>> {

        val loanDateInstant = Instant.now()

        val oldRepaymentSchedule = repaymentScheduleService.getOne(id)!!

        val interestProduct = interestProductFeatureService.findByProductId(productId)

        val repaymentFeature = repaymentFeatureService.getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("productId"), productId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.ofSize(1)).firstOrNull()

        val repaymentDayType = repaymentFeature?.payment?.repaymentDayType ?: RepaymentDayType.BASE_LOAN_DAY
        var repaymentDay = 21;
        if (RepaymentDayType.BASE_LOAN_DAY == repaymentDayType) {
            repaymentDay = loanDateInstant.toDateTime().dayOfMonth().get()
        }

        val newRepaymentSchedule = repaymentScheduleCalcGeneration.calculatorReset(
            DTORepaymentScheduleResetCalculate(
                remainLoanAmount = remainLoanAmount,
                repaymentDate = CalcDateComponent.parseViewToInstant(repaymentDate),
                repaymentDay = repaymentDay,
                baseYearDays = interestProduct?.interest?.baseYearDays!!,
                paymentMethod = repaymentFeature?.payment?.paymentMethod!!,
                repaymentFrequency = repaymentFeature.payment.frequency,
                oldRepaymentSchedule = oldRepaymentSchedule
            )
        )
        val savedRepaymentSchedule = repaymentScheduleService.updateOne(oldRepaymentSchedule, newRepaymentSchedule)
        val dtoRepaymentScheduleView = changeMapper(savedRepaymentSchedule)
        return DTOResponseSuccess(dtoRepaymentScheduleView).response()
    }


    private fun changeMapper(repaymentSchedule: RepaymentSchedule): DTORepaymentScheduleView{
        val dtoRepaymentScheduleDetailView: MutableList<DTORepaymentScheduleDetailView> = ArrayList()
        for (schedule in repaymentSchedule.schedule) {
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