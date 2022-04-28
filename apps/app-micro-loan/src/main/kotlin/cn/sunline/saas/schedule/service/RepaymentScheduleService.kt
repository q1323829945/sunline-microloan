package cn.sunline.saas.repayment.schedule.service

import cn.sunline.saas.consumer.loan.dto.DTOLoanApplication
import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.interest.service.InterestRateService
import cn.sunline.saas.interest.service.RatePlanService
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.repayment.schedule.factory.RepaymentScheduleCalcGeneration
import cn.sunline.saas.repayment.schedule.invoke.LoanProductDirectoryService
import cn.sunline.saas.response.DTOResponseSuccess
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@Service
class RepaymentScheduleService(private val loanProductDirectoryService: LoanProductDirectoryService){
//
//    @Autowired
//    private lateinit var repaymentScheduleCalcGeneration: RepaymentScheduleCalcGeneration
//
//    @Autowired
//    private lateinit var ratePlanService: RatePlanService
//
//    @Autowired
//    private lateinit var interestRateService: InterestRateService
//
////    @Autowired
////    private lateinit var repaymentScheduleService: RepaymentScheduleService
//
//    @Autowired
//    private lateinit var repaymentScheduleDetailService: RepaymentScheduleDetailService


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun getInfo(productId:Long): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        return loanProductDirectoryService.getProductDirectory(productId)
    }


    private fun initiateUnderwriting(dtoLoanApplication: DTOLoanApplication) {
        DaprHelper.binding(
            "INITIATE_UNDERWRITING",
            "create",
            dtoLoanApplication
        )
    }

    //TODO 通过接口远程调用获取
    /*
fun calculate(productId: Long,amount: BigDecimal,term: Int): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>> {
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


    fun save(dtoRepaymentSchedule: DTORepaymentScheduleAdd): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>> {
        val repaymentSchedule = objectMapper.convertValue<RepaymentSchedule>(dtoRepaymentSchedule)
        val schedule = repaymentScheduleService.save(repaymentSchedule)
        val scheduleDetail = repaymentScheduleDetailService.save(repaymentSchedule.repaymentScheduleDetail)
        schedule.repaymentScheduleDetail += scheduleDetail;
        val responseInterestRate = objectMapper.convertValue<DTORepaymentScheduleView>(schedule)
        return DTOResponseSuccess(responseInterestRate).response()
    }
*/
}