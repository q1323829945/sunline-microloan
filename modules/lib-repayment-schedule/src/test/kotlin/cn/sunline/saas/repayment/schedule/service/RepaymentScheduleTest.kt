

package cn.sunline.saas.repayment.schedule.service

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.factory.RepaymentScheduleCalcGenerationService
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
class RepaymentScheduleTest(@Autowired private var repaymentScheduleCalcGenerationService: RepaymentScheduleCalcGenerationService,
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

    @Test
    fun `generationEqualPrincipalFixedDayOneMonth`() {

        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00).setScale(2,RoundingMode.HALF_UP),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(12.000000).setScale(6, RoundingMode.HALF_UP),
            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_MONTH,
            repaymentDay = 21
        )
        // 预览
        val plan = repaymentScheduleCalcGenerationService.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)

        // 保存
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        dtoRepaymentScheduleAdd.term = LoanTermType.ONE_YEAR
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        Assertions.assertThat(register).isNotNull
        formatPlan(register)
    }

    @Test
    fun `generationEqualPrincipalFixedDayThreeMonth`() {
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00).setScale(2,RoundingMode.HALF_UP),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(12.000000).setScale(6,RoundingMode.HALF_UP),
            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.THREE_MONTHS,
            repaymentDay = 21
        )
        // 预览
        val plan = repaymentScheduleCalcGenerationService.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)

        // 保存
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        dtoRepaymentScheduleAdd.term = LoanTermType.ONE_YEAR
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        Assertions.assertThat(register).isNotNull
        formatPlan(register)
    }

    @Test
    fun `generationEqualPrincipalFixedDaySixMonth`() {
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00).setScale(2,RoundingMode.HALF_UP),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(12.000000).setScale(6,RoundingMode.HALF_UP),
            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.SIX_MONTHS,
            repaymentDay = 21
        )
        // 预览
        val plan = repaymentScheduleCalcGenerationService.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)

        // 保存
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        dtoRepaymentScheduleAdd.term = LoanTermType.ONE_YEAR
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        Assertions.assertThat(register).isNotNull
        formatPlan(register)
    }

    @Test
    fun `generationEqualPrincipalFixedDayTwelveMonth`() {

        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00).setScale(2,RoundingMode.HALF_UP),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(12.000000).setScale(6,RoundingMode.HALF_UP),
            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_YEAR,
            repaymentDay = 21
        )

        // 预览
        val plan = repaymentScheduleCalcGenerationService.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)

        // 保存
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        dtoRepaymentScheduleAdd.term = LoanTermType.ONE_YEAR
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        Assertions.assertThat(register).isNotNull
        formatPlan(register)
    }

    @Test
    fun `generationEqualInstallmentFixedDayOneMonth`() {
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00).setScale(2,RoundingMode.HALF_UP),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(12.000000).setScale(6,RoundingMode.HALF_UP),
            paymentMethod = PaymentMethodType.EQUAL_INSTALLMENT,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_MONTH,
            repaymentDay = 21
        )

        // 预览
        val plan = repaymentScheduleCalcGenerationService.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)

        // 保存
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        dtoRepaymentScheduleAdd.term = LoanTermType.ONE_YEAR
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        Assertions.assertThat(register).isNotNull
        formatPlan(register)
    }

    @Test
    fun `generationEqualInstallmentFixedDayThreeMonth`() {

        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00).setScale(2,RoundingMode.HALF_UP),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(12.000000).setScale(6,RoundingMode.HALF_UP),
            paymentMethod = PaymentMethodType.EQUAL_INSTALLMENT,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.THREE_MONTHS,
            repaymentDay = 21
        )

        // 预览
        val plan = repaymentScheduleCalcGenerationService.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)

        // 保存
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        dtoRepaymentScheduleAdd.term = LoanTermType.ONE_YEAR
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        Assertions.assertThat(register).isNotNull
        formatPlan(register)
    }

    @Test
    fun `generationEqualInstallmentFixedDaySixMonth`() {
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00).setScale(2,RoundingMode.HALF_UP),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(12.000000).setScale(6,RoundingMode.HALF_UP),
            paymentMethod = PaymentMethodType.EQUAL_INSTALLMENT,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.SIX_MONTHS,
            repaymentDay = 21
        )

        // 预览
        val plan = repaymentScheduleCalcGenerationService.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)

        // 保存
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        dtoRepaymentScheduleAdd.term = LoanTermType.ONE_YEAR
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        Assertions.assertThat(register).isNotNull
        formatPlan(register)
    }

    @Test
    fun `generationEqualInstallmentFixedDayTwelveMonth`() {
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00).setScale(2,RoundingMode.HALF_UP),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(12.000000).setScale(6,RoundingMode.HALF_UP),
            paymentMethod = PaymentMethodType.EQUAL_INSTALLMENT,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_YEAR,
            repaymentDay = 21
        )

        // 预览
        val plan = repaymentScheduleCalcGenerationService.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)

        // 保存
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        dtoRepaymentScheduleAdd.term = LoanTermType.ONE_YEAR
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        Assertions.assertThat(register).isNotNull
        formatPlan(register)
    }

    @Test
    fun `generationPayInterestSchedulePrincipalMaturityFixedDayOneMonth`() {
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00).setScale(2,RoundingMode.HALF_UP),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(12.000000).setScale(6,RoundingMode.HALF_UP),
            paymentMethod = PaymentMethodType.PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_MONTH,
            repaymentDay = 21
        )

        // 预览
        val plan = repaymentScheduleCalcGenerationService.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)

        // 保存
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        dtoRepaymentScheduleAdd.term = LoanTermType.ONE_YEAR
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        Assertions.assertThat(register).isNotNull
        formatPlan(register)
    }

    @Test
    fun `generationPayInterestSchedulePrincipalMaturityFixedDayThreeMonth`() {

        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00).setScale(2,RoundingMode.HALF_UP),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(12.000000).setScale(6,RoundingMode.HALF_UP),
            paymentMethod = PaymentMethodType.PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.THREE_MONTHS,
            repaymentDay = 21
        )

        // 预览
        val plan = repaymentScheduleCalcGenerationService.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)

        // 保存
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        dtoRepaymentScheduleAdd.term = LoanTermType.ONE_YEAR
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        Assertions.assertThat(register).isNotNull
        formatPlan(register)
    }

    @Test
    fun `generationPayInterestSchedulePrincipalMaturityFixedDaySixMonth`() {
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00).setScale(2,RoundingMode.HALF_UP),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(12.000000).setScale(6,RoundingMode.HALF_UP),
            paymentMethod = PaymentMethodType.PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.SIX_MONTHS,
            repaymentDay = 21
        )

        // 预览
        val plan = repaymentScheduleCalcGenerationService.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)

        // 保存
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        dtoRepaymentScheduleAdd.term = LoanTermType.ONE_YEAR
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        Assertions.assertThat(register).isNotNull
        formatPlan(register)
    }

    @Test
    fun `generationPayInterestSchedulePrincipalMaturityFixedDayTwelveMonth`() {
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00).setScale(2,RoundingMode.HALF_UP),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(12.000000).setScale(6,RoundingMode.HALF_UP),
            paymentMethod = PaymentMethodType.PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_YEAR,
            repaymentDay = 21
        )

        // 预览
        val plan = repaymentScheduleCalcGenerationService.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)

        // 保存
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        dtoRepaymentScheduleAdd.term = LoanTermType.ONE_YEAR
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        Assertions.assertThat(register).isNotNull
        formatPlan(register)
    }

    @Test
    fun `generationOneOffRepaymentFixedDay`() {
        val startDate = DateTime(2022, 3, 21, 0, 0).toInstant()
        val endDate = DateTime(2023, 3, 21, 0, 0).toInstant()
        val dtoRepaymentScheduleCalculateTrial = DTORepaymentScheduleCalculateTrial(
            amount = BigDecimal(12000.00).setScale(2,RoundingMode.HALF_UP),
            term = LoanTermType.ONE_YEAR,
            interestRate = BigDecimal(12.000000).setScale(6,RoundingMode.HALF_UP),
            paymentMethod = PaymentMethodType.ONE_OFF_REPAYMENT,
            startDate = startDate,
            endDate = endDate,
            repaymentDay = 21
        )

        // 预览
        val plan = repaymentScheduleCalcGenerationService.calculator(dtoRepaymentScheduleCalculateTrial)
        Assertions.assertThat(plan).isNotNull
        formatPlanView(plan)

        // 保存
        val dtoRepaymentScheduleAdd = objectMapper.convertValue<DTORepaymentScheduleAdd>(plan)
        dtoRepaymentScheduleAdd.term = LoanTermType.ONE_YEAR
        val register = repaymentScheduleService.register(dtoRepaymentScheduleAdd)
        Assertions.assertThat(register).isNotNull
        formatPlan(register)
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
        for (schedule in repaymentSchedule.schedule) {
            val lbmStr =
                        "\t the " + schedule.period + " period" +
                        "\t repaymentInstallment: " + schedule.installment +
                        "\t principal: " + schedule.principal +
                        "\t interest: " + schedule.interest +
                        "\t repaymentDate: " + schedule.repaymentDate
            allLoansStr += lbmStr + "\n\r"
        }
        logger.info("\t scheduleId: ${repaymentSchedule.id} " + "\t interestRate: ${repaymentSchedule.interestRate} \n\r " + "$allLoansStr")
    }

    private fun formatPlanView(repaymentSchedule: DTORepaymentScheduleView) {
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