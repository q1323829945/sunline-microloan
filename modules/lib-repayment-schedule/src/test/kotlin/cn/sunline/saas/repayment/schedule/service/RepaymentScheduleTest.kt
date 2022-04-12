package cn.sunline.saas.repayment.schedule.service

import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.fee.model.dto.DTOFeeFeatureAdd
import cn.sunline.saas.global.constant.*
import cn.sunline.saas.interest.constant.BaseYearDays
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.dto.DTOInterestFeatureAdd
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.dto.DTOAmountLoanProductConfiguration
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductAdd
import cn.sunline.saas.loan.product.model.dto.DTOTermLoanProductConfiguration
import cn.sunline.saas.loan.product.service.LoanProductService
import cn.sunline.saas.repayment.model.dto.DTOPrepaymentFeatureModalityAdd
import cn.sunline.saas.repayment.model.dto.DTORepaymentFeatureAdd
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.component.CalcInterestComponent
import cn.sunline.saas.repayment.schedule.component.CalcRepaymentInstallmentComponent
import cn.sunline.saas.repayment.schedule.factory.RepaymentScheduleCalcGeneration
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.DTOInterestCalculator
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleCalculate
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleResetCalculate
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.math.RoundingMode

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RepaymentScheduleTest(@Autowired private var repaymentScheduleCalcGeneration: RepaymentScheduleCalcGeneration,
                            @Autowired private var repaymentScheduleDetailService: RepaymentScheduleDetailService,
                            @Autowired private var repaymentScheduleService: RepaymentScheduleService,
                            @Autowired val loanProductService: LoanProductService
) {

    @Test
    fun `entity save`() {

        val amountConfiguration = DTOAmountLoanProductConfiguration(
            maxValueRange = "10",
            minValueRange = "0"
        )

        val termConfiguration = DTOTermLoanProductConfiguration(
            maxValueRange = LoanTermType.ONE_YEAR,
            minValueRange = null
        )

        val interestFeature = DTOInterestFeatureAdd(
            interestType = InterestType.FIXED,
            ratePlanId = 1000,
            baseYearDays = BaseYearDays.ACCOUNT_YEAR,
            adjustFrequency = "NOW",
            overdueInterestRatePercentage = 150
        )

        val prepayments = mutableListOf <DTOPrepaymentFeatureModalityAdd>()
        val prepayment1  = DTOPrepaymentFeatureModalityAdd(
            LoanTermType.ONE_MONTH,
            PrepaymentType.NOT_ALLOWED,
            null
        )

        val prepayment2  = DTOPrepaymentFeatureModalityAdd(
            LoanTermType.THREE_MONTHS,
            PrepaymentType.PARTIAL_PREPAYMENT,
            "1.5"
        )

        val prepayment3  = DTOPrepaymentFeatureModalityAdd(
            LoanTermType.SIX_MONTHS,
            PrepaymentType.FULL_REDEMPTION,
            null
        )

        prepayments.add(prepayment1)
        prepayments.add(prepayment2)
        prepayments.add(prepayment3)

        val repaymentFeature = DTORepaymentFeatureAdd(
            PaymentMethodType.ONE_OFF_REPAYMENT,
            RepaymentFrequency.ONE_MONTH,
            RepaymentDayType.BASE_LOAN_DAY,
            prepayments
        )

        val feeFeatures = mutableListOf<DTOFeeFeatureAdd>()
        val feeFeature1 = DTOFeeFeatureAdd(
            feeType = "Test1",
            feeMethodType = FeeMethodType.FIX_AMOUNT,
            feeAmount = "150",
            feeRate = null,
            feeDeductType = FeeDeductType.IMMEDIATE
        )
        val feeFeature2 = DTOFeeFeatureAdd(
            feeType = "Test2",
            feeMethodType = FeeMethodType.FEE_RATIO,
            feeAmount = null,
            feeRate = "1.5",
            feeDeductType = FeeDeductType.IMMEDIATE
        )

        feeFeatures.add(feeFeature1)
        feeFeatures.add(feeFeature2)


        val loanProduct = DTOLoanProductAdd(
            identificationCode = "SN0001",
            name = "Micro Loan",
            version = "1.0.0",
            description = "test",
            loanProductType = LoanProductType.CONSUMER_LOAN,
            loanPurpose = "test",
            amountConfiguration = amountConfiguration,
            termConfiguration = termConfiguration,
            interestFeature = interestFeature,
            repaymentFeature = repaymentFeature,
            feeFeatures = feeFeatures
        )

        val actual = loanProductService.register(loanProduct)


        Assertions.assertThat(actual).isNotNull
        //assertThat(actual.configurationOptions?.size).isEqualTo(2)
    }

/*
    @Test
    fun calcBaseInterest(){
        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)

        val result = CalcInterestComponent.calcBaseInterest(DTOInterestCalculator(
            calcAmount = BigDecimal(10000),
            loanRateMonth = BigDecimal(0.12),
            loanRateDay = BigDecimal(0.01),
            currentRepaymentDateTime = startDate,
            nextRepaymentDateTime = endDate
        ))
        Assertions.assertThat(result).isEqualTo(repaymentDate)
    }
*/

    /*
    @Test
    fun calcDayInterest(){
        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)

        val result = CalcInterestComponent.calcDayInterest(
            BigDecimal(10000),
            BigDecimal(0.01),
            startDate,
            endDate
        )
        Assertions.assertThat(result).isEqualTo(repaymentDate)
    }
     */

    @Test
    fun calcNextRepaymentDateTime(){
        val startDate = DateTime(2022, 3, 21, 0, 0)
        val endDate = DateTime(2023, 3, 21, 0, 0)
        val repaymentDate = DateTime(2022, 6, 21, 0, 0)

        val result = CalcDateComponent.calcNextRepaymentDateTime(startDate, endDate, repaymentDate)
        Assertions.assertThat(result).isEqualTo(repaymentDate)
    }

    /*
    @Test
    fun calcCapitalInterest(){
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
    */

    @Test
    fun calcBaseRepaymentInstallment(){
        val result = CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(
            BigDecimal(12000),
            BigDecimal(0.01),
        )
        Assertions.assertThat(result.setScale(2,RoundingMode.HALF_UP)).isEqualTo(BigDecimal(12000.01).setScale(2,RoundingMode.HALF_UP))
    }



    /**
     * 等额本金 指定还款日，按月
     */
    /*
    @Test
    fun generationAvgCapitalCalculatorFixedDayOneMonth() {

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
    */


    /**
     * 等额本金 指定还款日，按三个月
     */
    @Test
    fun generationAvgCapitalCalculatorFixedDayThreeMonth() {
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
    fun generationAvgCapitalCalculatorFixedDaySixMonth() {
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
    /*
    @Test
    fun generationAvgCapitalCalculatorAdvanceFixedDayOneMonth() {

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
*/

    /**
     *  等额本息 指定还款日，按月 提前还款
     */
    /*
    @Test
    fun generationCapitalInterestCalculatorAdvance() {
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
*/

    /**
     *  按期付息到期还款 指定还款日，按月 提前还款
     */
    /*
    @Test
    fun generationRepaymentPrincipalCalculatorAdvance() {

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
    */

    /**
     *  到期还本还息 指定还款日，按月 提前还款
     */
    @Test
    fun generationRepaymentPrincipalInterestCalculatorAdvance() {
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

    }
}
