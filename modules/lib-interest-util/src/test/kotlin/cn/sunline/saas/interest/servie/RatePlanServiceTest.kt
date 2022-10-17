package cn.sunline.saas.interest.servie

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.RatePlan
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.interest.service.RatePlanService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
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
class RatePlanServiceTest(@Autowired val ratePlanService: RatePlanService) {

    var standardId = 0L
    var customerId = 0L
    var termTierCustomerId = 0L
    var amountTierCustomerId = 0L

    @BeforeAll
    fun init() {
        ContextUtil.setTenant("123")

        val standardRates: MutableList<InterestRate> = mutableListOf()
        val customerRates: MutableList<InterestRate> = mutableListOf()
        val termTierCustomerRates: MutableList<InterestRate> = mutableListOf()
        val amountTierCustomerRates: MutableList<InterestRate> = mutableListOf()

        val standardRatePlanId = 1L
        val customerRatePlanId = 2L
        val termTierCustomerRatePlanId = 3L
        val amountTierCustomerRatePlanId = 4L

        val standardRate1 = InterestRate(1, LoanTermType.ONE_YEAR, null, BigDecimal(7.5), standardRatePlanId)
        val standardRate2 = InterestRate(2, LoanTermType.SIX_MONTHS, null, BigDecimal(6.5), standardRatePlanId)
        val standardRate3 = InterestRate(3, LoanTermType.THREE_MONTHS, null, BigDecimal(3.5), standardRatePlanId)

        val customerRate1 = InterestRate(4, LoanTermType.ONE_YEAR, null, BigDecimal(7.5), customerRatePlanId)
        val customerRate2 = InterestRate(5, LoanTermType.SIX_MONTHS, null, BigDecimal(6.5), customerRatePlanId)
        val customerRate3 = InterestRate(6, LoanTermType.THREE_MONTHS, null, BigDecimal(3.5), customerRatePlanId)

        val termTierCustomerRate1 = InterestRate(7, LoanTermType.ONE_YEAR, null, BigDecimal(7.5), termTierCustomerRatePlanId)
        val termTierCustomerRate2 = InterestRate(8, LoanTermType.SIX_MONTHS, null, BigDecimal(6.5), termTierCustomerRatePlanId)
        val termTierCustomerRate3 = InterestRate(9, LoanTermType.THREE_MONTHS, null, BigDecimal(3.5), termTierCustomerRatePlanId)

        val amountTierCustomerRate1 = InterestRate(10, null, BigDecimal(100000), BigDecimal(7.5), amountTierCustomerRatePlanId)
        val amountTierCustomerRate2 = InterestRate(11, null, BigDecimal(300000), BigDecimal(6.5), amountTierCustomerRatePlanId)
        val amountTierCustomerRate3 = InterestRate(12, null, BigDecimal(200000), BigDecimal(3.5), amountTierCustomerRatePlanId)

        standardRates.add(standardRate1)
        standardRates.add(standardRate2)
        standardRates.add(standardRate3)

        customerRates.add(customerRate1)
        customerRates.add(customerRate2)
        customerRates.add(customerRate3)

        termTierCustomerRates.add(termTierCustomerRate1)
        termTierCustomerRates.add(termTierCustomerRate2)
        termTierCustomerRates.add(termTierCustomerRate3)

        amountTierCustomerRates.add(amountTierCustomerRate1)
        amountTierCustomerRates.add(amountTierCustomerRate2)
        amountTierCustomerRates.add(amountTierCustomerRate3)


        val standardRatePlan = RatePlan(standardRatePlanId, "Test STANDARD Rate Plan", RatePlanType.STANDARD, standardRates)
        val standardActual = ratePlanService.save(standardRatePlan)

        val customerRatePlan = RatePlan(customerRatePlanId, "Test CUSTOMER Rate Plan", RatePlanType.CUSTOMER, customerRates)
        val customerActual = ratePlanService.save(customerRatePlan)

        val termTierCustomerRatePlan = RatePlan(termTierCustomerRatePlanId, "Test LOAN_TERM_TIER_CUSTOMER Rate Plan", RatePlanType.LOAN_TERM_TIER_CUSTOMER, termTierCustomerRates)
        val termTierCustomerActual = ratePlanService.save(termTierCustomerRatePlan)

        val amountTierCustomerRatePlan = RatePlan(amountTierCustomerRatePlanId, "Test LOAN_AMOUNT_TIER_CUSTOMER Rate Plan", RatePlanType.LOAN_AMOUNT_TIER_CUSTOMER, amountTierCustomerRates)
        val amountTierCustomerActual = ratePlanService.save(amountTierCustomerRatePlan)

        assertThat(standardActual).isNotNull
        assertThat(customerActual).isNotNull
        assertThat(termTierCustomerActual).isNotNull
        assertThat(amountTierCustomerActual).isNotNull


        standardId = standardActual.id!!
        customerId = customerActual.id!!
        termTierCustomerId = termTierCustomerActual.id!!
        amountTierCustomerId = amountTierCustomerActual.id!!
    }

    @Test
    fun `update STANDARD rate plan`() {
        val oldOne = ratePlanService.getOne(standardId)
        assertThat(oldOne).isNotNull

        val rates: MutableList<InterestRate> = mutableListOf()
        val standardRatePlanId = 1L
        val rate1 = InterestRate(1, LoanTermType.ONE_YEAR, null, BigDecimal(7.5), standardRatePlanId)
        val rate2 = InterestRate(2, LoanTermType.SIX_MONTHS, null, BigDecimal(6.5), standardRatePlanId)
        val rate3 = InterestRate(3, LoanTermType.THREE_MONTHS, null, BigDecimal(3.5), standardRatePlanId)
        val rate4 = InterestRate(13, LoanTermType.THREE_YEAR, null, BigDecimal(5.5), standardRatePlanId)

        rates.add(rate1)
        rates.add(rate2)
        rates.add(rate3)
        rates.add(rate4)

        val newOne = RatePlan(standardRatePlanId, "Test Update STANDARD Rate Plan", RatePlanType.STANDARD, rates)
        val actual = ratePlanService.updateOne(oldOne!!, newOne)

        assertThat(actual).isNotNull
        assertThat(actual.rates.size).isEqualTo(4)
    }

    @Test
    fun `update rate plan2`() {
        val oldOne = ratePlanService.getOne(customerId)

        assertThat(oldOne).isNotNull


        val rates: MutableList<InterestRate> = mutableListOf()
        val customerRatePlanId = 1L
        val customerRate1 = InterestRate(4, LoanTermType.ONE_YEAR, null, BigDecimal(7.5), customerRatePlanId)
        val customerRate2 = InterestRate(5, LoanTermType.SIX_MONTHS, null, BigDecimal(6.5), customerRatePlanId)
        val customerRate3 = InterestRate(6, LoanTermType.THREE_MONTHS, null, BigDecimal(3.5), customerRatePlanId)
        val customerRate4 = InterestRate(14, LoanTermType.THREE_MONTHS, null, BigDecimal(3.5), customerRatePlanId)

        rates.add(customerRate1)
        rates.add(customerRate2)
        rates.add(customerRate2)
        rates.add(customerRate4)

        val newOne = RatePlan(customerRatePlanId, "Test Rate Plan1", RatePlanType.CUSTOMER, rates)


        val actual = ratePlanService.updateOne(oldOne!!, newOne)

        assertThat(actual).isNotNull

        assertThat(actual.rates.size).isEqualTo(4)
    }

    @Test
    fun `find by type`() {
        val actual = ratePlanService.findByType(RatePlanType.STANDARD)

        assertThat(actual).isNotNull
    }
}