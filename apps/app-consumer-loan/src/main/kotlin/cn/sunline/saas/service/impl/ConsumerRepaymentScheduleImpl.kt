package cn.sunline.saas.service.impl

import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.interest.service.InterestFeatureService
import cn.sunline.saas.interest.service.InterestRateService
import cn.sunline.saas.interest.service.RatePlanService
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.repayment.schedule.factory.RepaymentScheduleCalcGeneration
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleAdd
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleCalculate
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleDetailView
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleView
import cn.sunline.saas.repayment.schedule.service.RepaymentScheduleDetailService
import cn.sunline.saas.repayment.schedule.service.RepaymentScheduleService
import cn.sunline.saas.repayment.service.RepaymentFeatureService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.service.ConsumerRepaymentScheduleService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.joda.time.Instant
import org.joda.time.format.DateTimeFormat
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
    private lateinit var interestRateService: InterestRateService

    @Autowired
    private lateinit var repaymentProductFeatureService: RepaymentFeatureService

    @Autowired
    private lateinit var repaymentScheduleService: RepaymentScheduleService

    @Autowired
    private lateinit var repaymentScheduleDetailService: RepaymentScheduleDetailService


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun initiate() {

    }

    override fun calculate(productId: Long,amount: BigDecimal,term: Int): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>> {
        val loanDateTime = Instant.now().toDateTime()
        val interestProduct = interestProductFeatureService.getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("productId"), productId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.ofSize(1))
        val first = interestProduct.firstOrNull()
        val ratePlanId = (first?.ratePlanId)!!

        val page = interestRateService.getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("ratePlanId"), ratePlanId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.ofSize(1))

        val interestRate = page.firstOrNull()

        val repaymentProductFeature = repaymentProductFeatureService.getPaged({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("productId"), productId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.ofSize(1))

        val repaymentProduct = repaymentProductFeature.firstOrNull()
        val repaymentDayType = repaymentProduct?.payment?.repaymentDayType ?: RepaymentDayType.BASE_LOAN_DAY
        var repaymentDay = 21;
        if (RepaymentDayType.BASE_LOAN_DAY == repaymentDayType) {
            repaymentDay = loanDateTime.dayOfMonth().get()
        }

        val repaymentSchedule = repaymentScheduleCalcGeneration.calculator(
            DTORepaymentScheduleCalculate(
                amount = amount,
                term = term,
                interestRate = interestRate?.rate!!,
                paymentMethod = repaymentProduct?.payment?.paymentMethod!!,
                startDate = loanDateTime,
                endDate = loanDateTime.plusMonths(term),
                repaymentFrequency = repaymentProduct.payment.frequency,
                baseYearDays = first.interest.baseYearDays,
                repaymentDay = repaymentDay,
                repaymentDayType = repaymentDayType
            )
        )
        val format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        val dtoRepaymentScheduleDetailView: MutableList<DTORepaymentScheduleDetailView> = ArrayList()
        for(schedule in repaymentSchedule.repaymentScheduleDetail){
            dtoRepaymentScheduleDetailView += DTORepaymentScheduleDetailView(
                id = schedule.id,
                installment = schedule.repaymentInstallment,
                period =  schedule.period,
                principal = schedule.principal,
                interest = schedule.interest,
                repaymentDate = schedule.repaymentDate.toString(format)
            )
        }

        val dtoRepaymentScheduleView = DTORepaymentScheduleView(
            repaymentScheduleId = repaymentSchedule.repaymentScheduleId,
            installment = repaymentSchedule.totalRepayment,
            interestRate = repaymentSchedule.interestRate,
            schedule = dtoRepaymentScheduleDetailView
        )

        return DTOResponseSuccess(dtoRepaymentScheduleView).response()

    }

    override fun save(dtoRepaymentSchedule: DTORepaymentScheduleAdd): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>> {
        val repaymentSchedule = objectMapper.convertValue<RepaymentSchedule>(dtoRepaymentSchedule)
        val schedule = repaymentScheduleService.save(repaymentSchedule)
        val scheduleDetail = repaymentScheduleDetailService.save(repaymentSchedule.repaymentScheduleDetail)
        schedule.repaymentScheduleDetail += scheduleDetail;
        val responseInterestRate = objectMapper.convertValue<DTORepaymentScheduleView>(schedule)
        return DTOResponseSuccess(responseInterestRate).response()
    }

//    fun updateOne(id: Long,dtoTemplate:DTORepaymentScheduleAdd): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>>{
//        val oldOne = repaymentScheduleService.getOne(id)?:throw Exception("Invalid template")
//        val newOne = objectMapper.convertValue<RepaymentSchedule>(dtoTemplate)
//        val scheduleDetail = repaymentScheduleDetailService.save(repaymentSchedule.repaymentScheduleDetail)
//        schedule.repaymentScheduleDetail += scheduleDetail;
//        val responseInterestRate = objectMapper.convertValue<DTORepaymentScheduleView>(schedule)
//        return DTOResponseSuccess(responseTemplate).response()
//    }
}