

package cn.sunline.saas.repayment.schedule.service

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.component.CalcRepaymentInstallmentComponent
import cn.sunline.saas.repayment.schedule.factory.RepaymentScheduleCalcGeneration
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.dto.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.joda.time.Instant
import org.joda.time.format.DateTimeFormat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.math.RoundingMode

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RepaymentScheduleTest(@Autowired private var repaymentScheduleCalcGeneration: RepaymentScheduleCalcGeneration,
                            @Autowired private var repaymentScheduleService: RepaymentScheduleService
) {

    private var logger = KotlinLogging.logger {}

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Test
    fun `calcNextRepaymentDateTime`(){
        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)

        val result = CalcDateComponent.calcNextRepaymentDateTime(startDate, endDate, repaymentDate)
        Assertions.assertThat(result).isEqualTo(repaymentDate)
    }

    /**
     * 等额本金 指定还款日，按月
     */
    @Test
    fun `generationAvgCapitalCalculatorFixedDayOneMonth`() {

        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_MONTH,
            repaymentDay = 21
        )

        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)
//        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
//        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
//        formatPlan(register)
    }


    /**
     * 等额本金 指定还款日，按三个月
     */
    @Test
    fun `generationAvgCapitalCalculatorFixedDayThreeMonth`() {
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.THREE_MONTHS,
            repaymentDay = 21
        )
        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculateTrial)
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        formatPlan(register)
    }

    /**
     * 等额本金 指定还款日，按六个月
     */
    @Test
    fun `generationAvgCapitalCalculatorFixedDaySixMonth`() {
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.SIX_MONTHS,
            repaymentDay = 21
        )
        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculateTrial)
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        formatPlan(register)
    }

    /**
     * 等额本金 指定还款日，按月 提前还款
     */
    @Test
    fun `generationAvgCapitalCalculatorAdvanceFixedDayOneMonth`() {

        // 3002316369063375008
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_MONTH,
            repaymentDay = 21
        )

        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculateTrial)
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        formatPlan(register)
        logger.info { "-----------------------------------更新前后-------------------------------------" }
//        val dtoRepaymentScheduleResetCalculate = DTORepaymentScheduleResetCalculate(
//            repaymentScheduleId = plan.repaymentScheduleId,
//            remainLoanAmount =  BigDecimal(12000.00),
//            loanRate = BigDecimal(0.120000),
//            baseYearDays = BaseYearDays.ACCOUNT_YEAR,
//            repaymentDate = repaymentDate,
//            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL
//        )
//        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculateTrialAdd)
//        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
//        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
//        formatPlan(register)
    }


    /**
     *  等额本息 指定还款日，按月 提前还款
     */
    @Test
    fun `generationCapitalInterestCalculatorAdvance`() {
        //1414654968295159232
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.EQUAL_INSTALLMENT,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_MONTH,
            repaymentDay = 21
        )

        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculateTrial)
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        formatPlan(register)
        logger.info { "-----------------------------------更新前后-------------------------------------" }
//        val dtoRepaymentScheduleResetCalculate = DTORepaymentScheduleResetCalculate(
//            repaymentScheduleId = plan.repaymentScheduleId,
//            remainLoanAmount =  BigDecimal(12000.00),
//            loanRate = BigDecimal(0.120000),
//            baseYearDays = BaseYearDays.ACCOUNT_YEAR,
//            repaymentDate = repaymentDate,
//            paymentMethod = PaymentMethodType.EQUAL_INSTALLMENT,
//        )
//        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculateTrialAdd)
//        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
//        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
//        formatPlan(register)
    }


    /**
     *  按期付息到期还款 指定还款日，按月 提前还款
     */
    @Test
    fun `generationRepaymentPrincipalCalculatorAdvance`() {

        // 1834102985732160007

        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_MONTH,
            repaymentDay = 21
        )

        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculateTrial)
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        formatPlan(register)
        logger.info { "-----------------------------------更新前后-------------------------------------" }
//        val dtoRepaymentScheduleResetCalculate = DTORepaymentScheduleResetCalculate(
//            repaymentScheduleId = plan.repaymentScheduleId,
//            remainLoanAmount =  BigDecimal(12000.00),
//            loanRate = BigDecimal(0.120000),
//            baseYearDays = BaseYearDays.ACCOUNT_YEAR,
//            repaymentDate = repaymentDate,
//            paymentMethod = PaymentMethodType.PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY
//        )
//        val plan1 =repaymentScheduleCalcGeneration.calculatorReset(dtoRepaymentScheduleResetCalculate)
//        formatPlan(plan1.repaymentScheduleDetail, plan1)
    }

    /**
     *  到期还本还息 指定还款日，按月 提前还款
     */
    @Test
    fun `generationRepaymentPrincipalInterestCalculatorAdvance`() {
        // 6977441507173528274
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.ONE_OFF_REPAYMENT,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_MONTH,
            repaymentDay = 21
        )
        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculateTrial)
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        formatPlan(register)
        logger.info { "-----------------------------------更新前后-------------------------------------" }
//        val dtoRepaymentScheduleResetCalculate = DTORepaymentScheduleResetCalculate(
//            repaymentScheduleId = plan.,
//            remainLoanAmount =  BigDecimal(12000.00),
//            loanRate = BigDecimal(0.120000),
//            baseYearDays = BaseYearDays.ACCOUNT_YEAR,
//            repaymentDate = repaymentDate,
//            paymentMethod = PaymentMethodType.ONE_OFF_REPAYMENT
//        )
//        val plan1 =repaymentScheduleCalcGeneration.calculatorReset(dtoRepaymentScheduleResetCalculate)
//        formatPlan(plan1.repaymentScheduleDetail, plan1)
    }


    @Test
    fun `register`() {
        val format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        val schedules : MutableList<DTORepaymentScheduleDetailAdd> = ArrayList()
        for( i in 0 until 3){
            schedules += DTORepaymentScheduleDetailAdd(
                period = i + 1,
                repaymentDate = DateTime(Instant.now()).plusMonths(1).toInstant().toString(format),
                installment = BigDecimal(10000),
                principal = BigDecimal(6000),
                interest = BigDecimal(4000)
            )
        }
        val repaymentSchedule = DTORepaymentScheduleAdd(
            installment = BigDecimal(30000),
            interestRate = BigDecimal(10.0),
            schedule = schedules,
            term = LoanTermType.ONE_YEAR
        )
        val register = repaymentScheduleService.register(repaymentSchedule)
        formatPlan(register)
    }


    private fun formatPlan(repaymentSchedule: RepaymentSchedule) {
        var allLoansStr = ""
        val format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        for (schedule in repaymentSchedule.schedule) {
            val lbmStr =
                        "\t the" + schedule.period + " period" +
                        "\t repaymentInstallment: " + schedule.installment +
                        "\t principal: " + schedule.principal +
                        "\t interest: " + schedule.interest +
                        "\t repaymentDate: " + schedule.repaymentDate
            allLoansStr += lbmStr + "\n\r"
        }
        logger.info("\t scheduleId: ${repaymentSchedule.id} " + "\t interestRate: ${repaymentSchedule.interestRate} \n\r " + "$allLoansStr")
    }

    private fun formatPlanView(repaymentSchedule: DTORepaymentScheduleTrialView) {
        var allLoansStr = ""
        for (schedule in repaymentSchedule.schedule) {
            val lbmStr =
                "\t the " + schedule.period + " period" +
                "\t repaymentInstallment: " + schedule.installment +
                "\t principal: " + schedule.principal +
                "\t interest: " + schedule.interest +
                "\t repaymentDate: " + schedule.repaymentDate
            allLoansStr += lbmStr + "\n\r"
        }
        logger.info ("\n\r interestRate: ${repaymentSchedule.interestRate} \n\r" + "$allLoansStr")
    }
}