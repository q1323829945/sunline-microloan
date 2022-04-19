package cn.sunline.saas.repayment.schedule.service

import cn.sunline.saas.global.constant.PaymentMethodType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.repayment.schedule.component.CalcInstallmentComponent
import cn.sunline.saas.repayment.schedule.model.dto.DTOPrincipalCalculator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.math.RoundingMode


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalcInstallmentComponentTest {

    @Test
    fun `calcBaseRepaymentInstallment`(){
        val principal = BigDecimal(10000.00)
        val interest = BigDecimal(500.00)
        val result = CalcInstallmentComponent.calcBaseRepaymentInstallment(principal,interest)
        Assertions.assertThat(result).isEqualTo(BigDecimal(10500.00).setScale(2,RoundingMode.HALF_UP))
    }


    @Test
    fun `calcCapitalInstallment`(){
        val result = CalcInstallmentComponent.calcCapitalInstallment(
            BigDecimal(10000),
            BigDecimal(0.01),
            12,
            RepaymentFrequency.ONE_MONTH
        )
        Assertions.assertThat(result).isEqualTo(BigDecimal(888.49).setScale(2,RoundingMode.HALF_UP))
    }

    @Test
    fun `calcBasePrincipalForEqualPrincipalOneMonth`(){
        val calcAmount = BigDecimal(100000.00)
        val periods = 12
        val period = 2
        val paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL
        val repaymentFrequency = RepaymentFrequency.ONE_MONTH
        val interestMonthRate = BigDecimal(0.01)
        val result = CalcInstallmentComponent.calcBasePrincipal(
            DTOPrincipalCalculator(
                calcAmount = calcAmount,
                paymentMethod = paymentMethod,
                interestRate = interestMonthRate,
                periods = periods,
                period = period,
                repaymentFrequency = repaymentFrequency
            )
        )
        Assertions.assertThat(result.setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal(8333.33).setScale(2,
            RoundingMode.HALF_UP))
    }

    @Test
    fun `calcBasePrincipalForEqualPrincipalThreeMonth`(){
        val calcAmount = BigDecimal(100000.00)
        val periods = 12
        val period = 2
        val paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL
        val repaymentFrequency = RepaymentFrequency.THREE_MONTHS
        val interestMonthRate = BigDecimal(0.01)
        val result = CalcInstallmentComponent.calcBasePrincipal(
            DTOPrincipalCalculator(
                calcAmount = calcAmount,
                paymentMethod = paymentMethod,
                interestRate = interestMonthRate,
                periods = periods,
                period = period,
                repaymentFrequency = repaymentFrequency
            )
        )
        Assertions.assertThat(result.setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal(8333.33).setScale(2,
            RoundingMode.HALF_UP))
    }

    @Test
    fun `calcBasePrincipalForEqualPrincipalSixMonth`(){
        val calcAmount = BigDecimal(100000.00)
        val periods = 12
        val period = 2
        val paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL
        val repaymentFrequency = RepaymentFrequency.SIX_MONTHS
        val interestMonthRate = BigDecimal(0.01)
        val result = CalcInstallmentComponent.calcBasePrincipal(
            DTOPrincipalCalculator(
                calcAmount = calcAmount,
                paymentMethod = paymentMethod,
                interestRate = interestMonthRate,
                periods = periods,
                period = period,
                repaymentFrequency = repaymentFrequency
            )
        )
        Assertions.assertThat(result.setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal(8333.33).setScale(2,
            RoundingMode.HALF_UP))
    }

    @Test
    fun `calcBasePrincipalForEqualPrincipalTwelveMonth`(){
        val calcAmount = BigDecimal(100000.00)
        val periods = 12
        val period = 2
        val paymentMethod = PaymentMethodType.EQUAL_PRINCIPAL
        val repaymentFrequency = RepaymentFrequency.TWELVE_MONTHS
        val interestMonthRate = BigDecimal(0.01)
        val result = CalcInstallmentComponent.calcBasePrincipal(
            DTOPrincipalCalculator(
                calcAmount = calcAmount,
                paymentMethod = paymentMethod,
                interestRate = interestMonthRate,
                periods = periods,
                period = period,
                repaymentFrequency = repaymentFrequency
            )
        )
        Assertions.assertThat(result.setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal(8333.33).setScale(2,
            RoundingMode.HALF_UP))
    }

    @Test
    fun `calcBasePrincipalForEqualInstallmentOneMonth`(){
        val calcAmount = BigDecimal(100000.00)
        val periods = 12
        val period = 2
        val paymentMethod = PaymentMethodType.EQUAL_INSTALLMENT
        val repaymentFrequency = RepaymentFrequency.ONE_MONTH
        val interestMonthRate = BigDecimal(0.01)
        val result = CalcInstallmentComponent.calcBasePrincipal(
            DTOPrincipalCalculator(
                calcAmount = calcAmount,
                paymentMethod = paymentMethod,
                interestRate = interestMonthRate,
                periods = periods,
                period = period,
                repaymentFrequency = repaymentFrequency
            )
        )
        Assertions.assertThat(result.setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal(7963.73).setScale(2,
            RoundingMode.HALF_UP))
    }

    @Test
    fun `calcBasePrincipalForEqualInstallmentThreeMonth`(){
        val calcAmount = BigDecimal(100000.00)
        val periods = 12
        val period = 2
        val paymentMethod = PaymentMethodType.EQUAL_INSTALLMENT
        val repaymentFrequency = RepaymentFrequency.THREE_MONTHS
        val interestMonthRate = BigDecimal(0.01)
        val result = CalcInstallmentComponent.calcBasePrincipal(
            DTOPrincipalCalculator(
                calcAmount = calcAmount,
                paymentMethod = paymentMethod,
                interestRate = interestMonthRate,
                periods = periods,
                period = period,
                repaymentFrequency = repaymentFrequency
            )
        )
        Assertions.assertThat(result.setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal(7257.59).setScale(2,
            RoundingMode.HALF_UP))
    }

    @Test
    fun `calcBasePrincipalForEqualInstallmentSixMonth`(){
        val calcAmount = BigDecimal(100000.00)
        val periods = 12
        val period = 2
        val paymentMethod = PaymentMethodType.EQUAL_INSTALLMENT
        val repaymentFrequency = RepaymentFrequency.SIX_MONTHS
        val interestMonthRate = BigDecimal(0.01)
        val result = CalcInstallmentComponent.calcBasePrincipal(
            DTOPrincipalCalculator(
                calcAmount = calcAmount,
                paymentMethod = paymentMethod,
                interestRate = interestMonthRate,
                periods = periods,
                period = period,
                repaymentFrequency = repaymentFrequency
            )
        )
        Assertions.assertThat(result.setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal(6283.37).setScale(2,
            RoundingMode.HALF_UP))
    }

    @Test
    fun `calcBasePrincipalForEqualInstallmentTwelveMonth`(){
        val calcAmount = BigDecimal(100000.00)
        val periods = 12
        val period = 2
        val paymentMethod = PaymentMethodType.EQUAL_INSTALLMENT
        val repaymentFrequency = RepaymentFrequency.TWELVE_MONTHS
        val interestMonthRate = BigDecimal(0.01)
        val result = CalcInstallmentComponent.calcBasePrincipal(
            DTOPrincipalCalculator(
                calcAmount = calcAmount,
                paymentMethod = paymentMethod,
                interestRate = interestMonthRate,
                periods = periods,
                period = period,
                repaymentFrequency = repaymentFrequency
            )
        )
        Assertions.assertThat(result.setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal(4640.92).setScale(2,
            RoundingMode.HALF_UP))
    }
}