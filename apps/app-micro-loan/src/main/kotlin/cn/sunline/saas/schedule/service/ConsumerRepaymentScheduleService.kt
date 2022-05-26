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



        val ratePlanId = loanProduct.interestFeature.ratePlanId
        val ratePlan = ratePlanService.getOne(ratePlanId.toLong())!!
        val rates = ratePlan.rates
        val interestRate = InterestRateHelper.getRate(term, rates)!!


        val repaymentFeature = loanProduct.repaymentFeature
        val repaymentDayType = repaymentFeature.payment.repaymentDayType
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

    fun register(dtoRepaymentSchedule: DTORepaymentScheduleAdd): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>> {
        //val savedRepaymentSchedule :RepaymentSchedule //= repaymentScheduleService.register(dtoRepaymentSchedule)
        val dtoRepaymentScheduleView = changeMapper(null)
        return DTOResponseSuccess(dtoRepaymentScheduleView).response()
    }

    fun updateOne(id: Long, productId: Long, repaymentDate: String, term: LoanTermType, remainLoanAmount: BigDecimal){

//        val loanDateInstant = Instant.now()
//
//        val oldRepaymentSchedule :RepaymentSchedule?//= repaymentScheduleService.getOne(id)!!
//
//        val interestProduct = interestProductFeatureService.findByProductId(productId)
//
//        val repaymentFeature = repaymentFeatureService.getPaged({ root, _, criteriaBuilder ->
//            val predicates = mutableListOf<Predicate>()
//            predicates.add(criteriaBuilder.equal(root.get<Long>("productId"), productId))
//            criteriaBuilder.and(*(predicates.toTypedArray()))
//        }, Pageable.ofSize(1)).firstOrNull()
//
//        val repaymentDayType = repaymentFeature?.payment?.repaymentDayType ?: RepaymentDayType.BASE_LOAN_DAY
//        var repaymentDay = 21;
//        if (RepaymentDayType.BASE_LOAN_DAY == repaymentDayType) {
//            repaymentDay = loanDateInstant.toDateTime().dayOfMonth().get()
//        }
//
//        val newRepaymentSchedule = repaymentScheduleCalcGeneration.calculatorReset(
//            DTORepaymentScheduleResetCalculate(
//                remainLoanAmount = remainLoanAmount,
//                repaymentDate = CalcDateComponent.parseViewToInstant(repaymentDate),
//                repaymentDay = repaymentDay,
//                baseYearDays = interestProduct?.interest?.baseYearDays!!,
//                paymentMethod = repaymentFeature?.payment?.paymentMethod!!,
//                repaymentFrequency = repaymentFeature.payment.frequency,
//                oldRepaymentSchedule = oldRepaymentSchedule!!
//            )
//        )
//        val savedRepaymentSchedule = repaymentScheduleService.updateOne(oldRepaymentSchedule, newRepaymentSchedule)
//        val dtoRepaymentScheduleView = changeMapper(savedRepaymentSchedule)
//        return DTOResponseSuccess(dtoRepaymentScheduleView).response()
    }


    private fun changeMapper(repaymentSchedule: RepaymentSchedule?): DTORepaymentScheduleView{
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