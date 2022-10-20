package cn.sunline.saas.loan.product.service

import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.fee.model.dto.DTOFeeFeatureAdd
import cn.sunline.saas.global.constant.*
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.dto.DTOInterestFeatureAdd
import cn.sunline.saas.interest.model.dto.DTOInterestFeatureModalityAdd
import cn.sunline.saas.interest.model.dto.DTOOverdueInterestFeatureModalityAdd
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.loan.product.model.dto.*
import cn.sunline.saas.repayment.model.dto.DTOPrepaymentFeatureModalityAdd
import cn.sunline.saas.repayment.model.dto.DTORepaymentFeatureAdd
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/**
 * @title: RatePlanServiceTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/9 13:55
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanProductServiceTest(@Autowired val loanProductService: LoanProductService) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    var productId = 0L

    @BeforeAll
    fun init() {
        ContextUtil.setTenant("123")

        val product = register()

        productId = product.id.toLong()
    }

    fun register(): DTOLoanProductView {

        val amountConfiguration = DTOAmountLoanProductConfiguration(
            id = null,
            maxValueRange = "10",
            minValueRange = "0"
        )

        val termConfiguration = DTOTermLoanProductConfiguration(
            id = null,
            maxValueRange = LoanTermType.ONE_YEAR,
            minValueRange = LoanTermType.ONE_MONTH
        )

        val interestFeature = DTOInterestFeatureAdd(
            interestType = InterestType.FIXED,
            ratePlanId = 1000,
            interest = DTOInterestFeatureModalityAdd(
                baseYearDays = BaseYearDays.ACCOUNT_YEAR,
                adjustFrequency = "NOW",
                floatPoint = BigDecimal(0.1),
                floatRatio = null
            ),
            overdueInterest = DTOOverdueInterestFeatureModalityAdd(
                overdueInterestRatePercentage = 150
            )
        )

        val prepayments = mutableListOf<DTOPrepaymentFeatureModalityAdd>()
        val prepayment1 = DTOPrepaymentFeatureModalityAdd(
            LoanTermType.ONE_MONTH,
            PrepaymentType.NOT_ALLOWED,
            "0"
        )

//        val prepayment2  = DTOPrepaymentFeatureModalityAdd(
//            LoanTermType.THREE_MONTHS,
//            PrepaymentType.PARTIAL_PREPAYMENT,
//            "1.5"
//        )

        val prepayment3 = DTOPrepaymentFeatureModalityAdd(
            LoanTermType.SIX_MONTHS,
            PrepaymentType.FULL_REDEMPTION,
            "0"
        )

        prepayments.add(prepayment1)
//        prepayments.add(prepayment2)
        prepayments.add(prepayment3)

        val repaymentFeature = DTORepaymentFeatureAdd(
            PaymentMethodType.ONE_OFF_REPAYMENT,
            RepaymentFrequency.ONE_MONTH,
            RepaymentDayType.BASE_LOAN_DAY,
            prepayments,
            0
        )

        val feeFeatures = mutableListOf<DTOFeeFeatureAdd>()
        val feeFeature1 = DTOFeeFeatureAdd(
            feeType = LoanFeeType.PREPAYMENT,
            feeMethodType = FeeMethodType.FIX_AMOUNT,
            feeAmount = BigDecimal("150"),
            feeRate = null,
            feeDeductType = FeeDeductType.IMMEDIATE
        )
        val feeFeature2 = DTOFeeFeatureAdd(
            feeType = LoanFeeType.OVERDUE,
            feeMethodType = FeeMethodType.FEE_RATIO,
            feeAmount = null,
            feeRate = BigDecimal("1.5"),
            feeDeductType = FeeDeductType.IMMEDIATE
        )

        feeFeatures.add(feeFeature1)
        feeFeatures.add(feeFeature2)


        val loanProduct = DTOLoanProduct(
            id = null,
            identificationCode = "SN0001",
            name = "Micro Loan",
            version = "1.0.0",
            description = "test",
            loanProductType = LoanProductType.CONSUMER_LOAN,
            loanPurpose = "test",
            businessUnit = "123",
            status = BankingProductStatus.INITIATED,
            amountConfiguration = amountConfiguration,
            termConfiguration = termConfiguration,
            interestFeature = objectMapper.convertValue(interestFeature),
            repaymentFeature = repaymentFeature.let { objectMapper.convertValue(it) },
            feeFeatures = feeFeatures.let { objectMapper.convertValue<MutableList<DTOFeeFeature>>(it) },
            loanUploadConfigureFeatures = listOf()
        )
        val actual = loanProductService.register(loanProduct)


        assertThat(actual).isNotNull

        return actual
    }

    @Test
    @Transactional
    fun `get product`() {
        val product = loanProductService.getLoanProduct(productId)

        assertThat(product).isNotNull
    }

    @Test
    fun `get paged`() {
        val paged = loanProductService.getLoanProductPaged(null, null, null, null, Pageable.unpaged())
        assertThat(paged.size).isEqualTo(1)
    }

    @Test
    @Transactional
    fun `find by identification code`() {
        val products = loanProductService.findByIdentificationCode("SN0001")
        assertThat(products.size).isEqualTo(1)
    }

//    @Test
//    @Transactional
//    fun `find by identification code and status`() {
//        val products = loanProductService.findByIdentificationCodeAndStatus("SN0001", BankingProductStatus.INITIATED)
//        assertThat(products.size).isEqualTo(1)
//    }

    @Test
    @Transactional
    fun `update product`() {
        val amountConfiguration = DTOAmountLoanProductConfiguration(
            id = null,
            maxValueRange = "10",
            minValueRange = "0"
        )

        val termConfiguration = DTOTermLoanProductConfiguration(
            id = null,
            maxValueRange = LoanTermType.ONE_YEAR,
            minValueRange = LoanTermType.ONE_MONTH
        )

        val interestFeature = DTOInterestFeatureAdd(
            interestType = InterestType.FIXED,
            ratePlanId = 1000,
            interest = DTOInterestFeatureModalityAdd(
                baseYearDays = BaseYearDays.ACCOUNT_YEAR,
                adjustFrequency = "NOW",
                floatPoint = BigDecimal(0.1),
                floatRatio = null
            ),
            overdueInterest = DTOOverdueInterestFeatureModalityAdd(
                overdueInterestRatePercentage = 150
            )
        )

        val prepayments = mutableListOf<DTOPrepaymentFeatureModalityAdd>()
        val prepayment1 = DTOPrepaymentFeatureModalityAdd(
            LoanTermType.ONE_MONTH,
            PrepaymentType.NOT_ALLOWED,
            "0"
        )

        val prepayment3 = DTOPrepaymentFeatureModalityAdd(
            LoanTermType.SIX_MONTHS,
            PrepaymentType.FULL_REDEMPTION,
            "0"
        )

        prepayments.add(prepayment1)
        prepayments.add(prepayment3)

        val repaymentFeature = DTORepaymentFeatureAdd(
            PaymentMethodType.ONE_OFF_REPAYMENT,
            RepaymentFrequency.ONE_MONTH,
            RepaymentDayType.BASE_LOAN_DAY,
            prepayments,
            0
        )

        val feeFeatures = mutableListOf<DTOFeeFeatureAdd>()
        val feeFeature1 = DTOFeeFeatureAdd(
            feeType = LoanFeeType.PREPAYMENT,
            feeMethodType = FeeMethodType.FIX_AMOUNT,
            feeAmount = BigDecimal("150"),
            feeRate = null,
            feeDeductType = FeeDeductType.IMMEDIATE
        )
        val feeFeature2 = DTOFeeFeatureAdd(
            feeType = LoanFeeType.OVERDUE,
            feeMethodType = FeeMethodType.FEE_RATIO,
            feeAmount = null,
            feeRate = BigDecimal("1.5"),
            feeDeductType = FeeDeductType.IMMEDIATE
        )

        feeFeatures.add(feeFeature1)
        feeFeatures.add(feeFeature2)


        val loanProduct = DTOLoanProduct(
            id = productId.toString(),
            identificationCode = "SN0001",
            name = "Micro Loan",
            version = "1.0.0",
            description = "test2",
            loanProductType = LoanProductType.CONSUMER_LOAN,
            loanPurpose = "test",
            businessUnit = "123",
            status = BankingProductStatus.INITIATED,
            amountConfiguration = amountConfiguration,
            termConfiguration = termConfiguration,
            interestFeature = objectMapper.convertValue(interestFeature),
            repaymentFeature = repaymentFeature.let { objectMapper.convertValue(it) },
            feeFeatures = feeFeatures.let { objectMapper.convertValue<MutableList<DTOFeeFeature>>(it) },
            loanUploadConfigureFeatures = listOf()
        )

        val product = loanProductService.updateLoanProduct(productId, loanProduct)


        assertThat(product).isNotNull
        assertThat(product.description).isEqualTo("test2")
    }
}