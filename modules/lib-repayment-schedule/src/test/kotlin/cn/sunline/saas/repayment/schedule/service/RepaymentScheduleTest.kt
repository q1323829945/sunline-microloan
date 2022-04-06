

package cn.sunline.saas.repayment.schedule.service

import cn.sunline.saas.interest.model.BaseYearDays
import cn.sunline.saas.repayment.model.PaymentMethodType
import cn.sunline.saas.repayment.model.RepaymentFrequency
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.component.CalcInterestComponent
import cn.sunline.saas.repayment.schedule.component.CalcRepaymentInstallmentComponent
import cn.sunline.saas.repayment.schedule.factory.RepaymentScheduleCalcGeneration
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.DTOInterestCalculator
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleCalculate
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleResetCalculate
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.math.RoundingMode

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RepaymentScheduleTest(@Autowired private var repaymentScheduleCalcGeneration: RepaymentScheduleCalcGeneration,
                            @Autowired private var repaymentScheduleDetailService: RepaymentScheduleDetailService,
                            @Autowired private var repaymentScheduleService: RepaymentScheduleService
) {

    private var logger = KotlinLogging.logger {}

    @Test
    fun `calcNextRepaymentDateTime`(){
        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)

        val result = CalcDateComponent.calcNextRepaymentDateTime(startDate, endDate, repaymentDate)
        Assertions.assertThat(result).isEqualTo(repaymentDate)
    }

    @Test
    fun `calcCapitalInterest`(){
        val result1 = CalcRepaymentInstallmentComponent.calcCapitalInstallment(
            BigDecimal(12000),
            BigDecimal(0.01),
            12,
            RepaymentFrequency.ONE_MONTH
        )
        Assertions.assertThat(result1.setScale(2,RoundingMode.HALF_UP)).isEqualTo(BigDecimal(1066.19).setScale(2,RoundingMode.HALF_UP))

        val result2 = CalcRepaymentInstallmentComponent.calcCapitalInstallment(
            BigDecimal(12000),
            BigDecimal(0.01),
            13,
            RepaymentFrequency.ONE_MONTH
        )
        Assertions.assertThat(result2.setScale(2,RoundingMode.HALF_UP)).isEqualTo(BigDecimal(988.98).setScale(2,RoundingMode.HALF_UP))
    }


    @Test
    fun `calcBaseRepaymentInstallment`(){
        val result = CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(
            BigDecimal(12000),
            BigDecimal(0.01),
        )
        Assertions.assertThat(result.setScale(2,RoundingMode.HALF_UP)).isEqualTo(BigDecimal(12000.01).setScale(2,RoundingMode.HALF_UP))
    }



    /**
     * 等额本金 指定还款日，按月
     */
    @Test
    fun `generationAvgCapitalCalculatorFixedDayOneMonth`() {

        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val dtoRepaymentScheduleCalculate = DTORepaymentScheduleCalculate(
            amount = BigDecimal(12000.00),
            term = 12,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_MONTH,
            repaymentDay = 21
        )

        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculate)
        repaymentScheduleService.save(plan)
        repaymentScheduleDetailService.save(plan.repaymentScheduleDetail)
        formatPlan(plan.repaymentScheduleDetail, plan)
    }


    /**
     * 等额本金 指定还款日，按三个月
     */
    @Test
    fun `generationAvgCapitalCalculatorFixedDayThreeMonth`() {
        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val dtoRepaymentScheduleCalculate = DTORepaymentScheduleCalculate(
            amount = BigDecimal(12000.00),
            term = 12,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.THREE_MONTHS,
            repaymentDay = 21
        )
        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculate)
        formatPlan(plan.repaymentScheduleDetail, plan)
    }

    /**
     * 等额本金 指定还款日，按六个月
     */
    @Test
    fun `generationAvgCapitalCalculatorFixedDaySixMonth`() {
        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val dtoRepaymentScheduleCalculate = DTORepaymentScheduleCalculate(
            amount = BigDecimal(12000.00),
            term = 12,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.SIX_MONTHS,
            repaymentDay = 21
        )
        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculate)
        formatPlan(plan.repaymentScheduleDetail, plan)
    }

    /**
     * 等额本金 指定还款日，按月 提前还款
     */
    @Test
    fun `generationAvgCapitalCalculatorAdvanceFixedDayOneMonth`() {

        // 3002316369063375008
        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)
        val dtoRepaymentScheduleCalculate = DTORepaymentScheduleCalculate(
            amount = BigDecimal(12000.00),
            term = 12,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_MONTH,
            repaymentDay = 21
        )

        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculate)
        repaymentScheduleService.save(plan)
        repaymentScheduleDetailService.save(plan.repaymentScheduleDetail)
        formatPlan(plan.repaymentScheduleDetail, plan)
        logger.info { "-----------------------------------更新前后-------------------------------------" }
        val dtoRepaymentScheduleResetCalculate = DTORepaymentScheduleResetCalculate(
            repaymentScheduleId = plan.repaymentScheduleId,
            remainLoanAmount =  BigDecimal(12000.00),
            loanRate = BigDecimal(0.120000),
            baseYearDays = BaseYearDays.ACCOUNT_YEAR,
            repaymentDate = repaymentDate,
            paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL
        )
        val plan1 = repaymentScheduleCalcGeneration.calculatorReset(dtoRepaymentScheduleResetCalculate)
        formatPlan(plan1.repaymentScheduleDetail, plan1)
    }


    /**
     *  等额本息 指定还款日，按月 提前还款
     */
    @Test
    fun `generationCapitalInterestCalculatorAdvance`() {
        //1414654968295159232
        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)
        val dtoRepaymentScheduleCalculate = DTORepaymentScheduleCalculate(
            amount = BigDecimal(12000.00),
            term = 12,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.EQUAL_INSTALLMENT,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_MONTH,
            repaymentDay = 21
        )

        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculate)
        repaymentScheduleService.save(plan)
        repaymentScheduleDetailService.save(plan.repaymentScheduleDetail)
        formatPlan(plan.repaymentScheduleDetail, plan)
        logger.info { "-----------------------------------更新前后-------------------------------------" }
        val dtoRepaymentScheduleResetCalculate = DTORepaymentScheduleResetCalculate(
            repaymentScheduleId = plan.repaymentScheduleId,
            remainLoanAmount =  BigDecimal(12000.00),
            loanRate = BigDecimal(0.120000),
            baseYearDays = BaseYearDays.ACCOUNT_YEAR,
            repaymentDate = repaymentDate,
            paymentMethod = PaymentMethodType.EQUAL_INSTALLMENT,
        )
        val plan1 =repaymentScheduleCalcGeneration.calculatorReset(dtoRepaymentScheduleResetCalculate)
        formatPlan(plan1.repaymentScheduleDetail, plan1)
    }


    /**
     *  按期付息到期还款 指定还款日，按月 提前还款
     */
    @Test
    fun `generationRepaymentPrincipalCalculatorAdvance`() {

        // 1834102985732160007

        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)
        val dtoRepaymentScheduleCalculate = DTORepaymentScheduleCalculate(
            amount = BigDecimal(12000.00),
            term = 12,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_MONTH,
            repaymentDay = 21
        )

        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculate)
        repaymentScheduleService.save(plan)
        repaymentScheduleDetailService.save(plan.repaymentScheduleDetail)
        formatPlan(plan.repaymentScheduleDetail, plan)
        logger.info { "-----------------------------------更新前后-------------------------------------" }
        val dtoRepaymentScheduleResetCalculate = DTORepaymentScheduleResetCalculate(
            repaymentScheduleId = plan.repaymentScheduleId,
            remainLoanAmount =  BigDecimal(12000.00),
            loanRate = BigDecimal(0.120000),
            baseYearDays = BaseYearDays.ACCOUNT_YEAR,
            repaymentDate = repaymentDate,
            paymentMethod = PaymentMethodType.PAY_INTEREST_SCHEDULE_PRINCIPAL_MATURITY
        )
        val plan1 =repaymentScheduleCalcGeneration.calculatorReset(dtoRepaymentScheduleResetCalculate)
        formatPlan(plan1.repaymentScheduleDetail, plan1)
    }

    /**
     *  到期还本还息 指定还款日，按月 提前还款
     */
    @Test
    fun `generationRepaymentPrincipalInterestCalculatorAdvance`() {
        // 6977441507173528274
        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)
        val dtoRepaymentScheduleCalculate = DTORepaymentScheduleCalculate(
            amount = BigDecimal(12000.00),
            term = 12,
            interestRate = BigDecimal(0.120000),
            paymentMethod = PaymentMethodType.ONE_OFF_REPAYMENT,
            startDate = startDate,
            endDate = endDate,
            repaymentFrequency = RepaymentFrequency.ONE_MONTH,
            repaymentDay = 21
        )
        val plan = repaymentScheduleCalcGeneration.calculator(dtoRepaymentScheduleCalculate)
        repaymentScheduleService.save(plan)
        repaymentScheduleDetailService.save(plan.repaymentScheduleDetail)
        formatPlan(plan.repaymentScheduleDetail, plan)
        logger.info { "-----------------------------------更新前后-------------------------------------" }
        val dtoRepaymentScheduleResetCalculate = DTORepaymentScheduleResetCalculate(
            repaymentScheduleId = plan.repaymentScheduleId,
            remainLoanAmount =  BigDecimal(12000.00),
            loanRate = BigDecimal(0.120000),
            baseYearDays = BaseYearDays.ACCOUNT_YEAR,
            repaymentDate = repaymentDate,
            paymentMethod = PaymentMethodType.ONE_OFF_REPAYMENT
        )
        val plan1 =repaymentScheduleCalcGeneration.calculatorReset(dtoRepaymentScheduleResetCalculate)
        formatPlan(plan1.repaymentScheduleDetail, plan1)
    }



    private fun formatPlan(repaymentSchedulePerMonths: List<RepaymentScheduleDetail>, repaymentSchedule: RepaymentSchedule) {
        var allLoansStr = ""
        val format = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        for (repaymentSchedulePerMonth in repaymentSchedulePerMonths) {
            val lbmStr =
                "\t第" + repaymentSchedulePerMonth.period + "期" +
                        "\t还款金额: " + repaymentSchedulePerMonth.repaymentInstallment +
                        "\t本金: " + repaymentSchedulePerMonth.principal +
                        "\t利息: " + repaymentSchedulePerMonth.interest +
                        "\t剩余：" + repaymentSchedulePerMonth.remainPrincipal +
                        "\t还款日期：" + repaymentSchedulePerMonth.repaymentDate +
                        "\t还款日期：" + repaymentSchedulePerMonth.repaymentDate
            if (allLoansStr == "") {
                allLoansStr = lbmStr
            } else {
                allLoansStr += """
              
              $lbmStr
              """.trimIndent()
            }
        }
        logger.info (
            " \t还款计划编号：${repaymentSchedule.repaymentScheduleId} \t总贷款: ${repaymentSchedule.amount}\n\r" +
                    "\t期数: ${repaymentSchedule.term}	\t贷款利率: ${repaymentSchedule.interestRate} \t总利息: ${repaymentSchedule.totalInterest}	\n\r" +
                    "\t还款总额：${repaymentSchedule.totalRepayment} " +
                    "\n\r $allLoansStr"
        )
    }
}