package cn.sunline.saas.loan.product.service

import cn.sunline.saas.fee.model.dto.DTOFeeFeatureAdd
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.dto.DTOInterestFeatureAdd
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.dto.DTOAmountLoanProductConfiguration
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductAdd
import cn.sunline.saas.loan.product.model.dto.DTOTermLoanProductConfiguration
import cn.sunline.saas.repayment.model.dto.DTOPrepaymentFeatureModalityAdd
import cn.sunline.saas.repayment.model.dto.DTORepaymentFeatureAdd
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

/**
 * @title: RatePlanServiceTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/9 13:55
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanProductServiceTest(@Autowired val loanProductService: LoanProductService) {


    @Test
    fun `entity save`() {

        val amountConfiguration = DTOAmountLoanProductConfiguration(
            maxValueRange = BigDecimal.TEN,
            minValueRange = BigDecimal.ZERO
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
            BigDecimal(1.5)
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
            feeAmount = BigDecimal(150),
            feeRate = null,
            feeDeductType = FeeDeductType.IMMEDIATE
        )
        val feeFeature2 = DTOFeeFeatureAdd(
            feeType = "Test2",
            feeMethodType = FeeMethodType.FEE_RATIO,
            feeAmount = null,
            feeRate = BigDecimal(1.5),
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


        assertThat(actual).isNotNull
        //assertThat(actual.configurationOptions?.size).isEqualTo(2)
    }
}