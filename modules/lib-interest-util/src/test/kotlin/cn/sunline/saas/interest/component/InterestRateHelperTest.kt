package cn.sunline.saas.interest.component

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.InterestRate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * @title: InterestRateHelperTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/19 10:33
 */
class InterestRateHelperTest {

    @Test
    fun `get term rate`() {
        val rates = mutableListOf<InterestRate>(
            InterestRate(1, LoanTermType.ONE_MONTH, null, BigDecimal("1.6"), 1),
            InterestRate(2, LoanTermType.THREE_MONTHS, null, BigDecimal("1.8"), 1),
            InterestRate(3, LoanTermType.SIX_MONTHS, null, BigDecimal("2.1"), 1),
            InterestRate(4, LoanTermType.ONE_YEAR, null, BigDecimal("2.5"), 1),
            InterestRate(5, LoanTermType.TWO_YEAR, null, BigDecimal("3.5"), 1),
            InterestRate(6, LoanTermType.THREE_YEAR, null, BigDecimal("4.5"), 1)

        )

        val actual1 = InterestRateHelper.getRate(BigDecimal.ZERO, LoanTermType.ONE_MONTH, rates)
        assertThat(actual1).isEqualTo(BigDecimal("1.6"))

        val actual2 = InterestRateHelper.getRate(BigDecimal.ZERO, LoanTermType.THREE_MONTHS, rates)
        assertThat(actual2).isEqualTo(BigDecimal("1.8"))

        val actual3 = InterestRateHelper.getRate(BigDecimal.ZERO, LoanTermType.SIX_MONTHS, rates)
        assertThat(actual3).isEqualTo(BigDecimal("2.1"))

        val actual4 = InterestRateHelper.getRate(BigDecimal.ZERO, LoanTermType.ONE_YEAR, rates)
        assertThat(actual4).isEqualTo(BigDecimal("2.5"))

        val actual5 = InterestRateHelper.getRate(BigDecimal.ZERO, LoanTermType.TWO_YEAR, rates)
        assertThat(actual5).isEqualTo(BigDecimal("3.5"))

        val actual6 = InterestRateHelper.getRate(BigDecimal.ZERO, LoanTermType.THREE_YEAR, rates)
        assertThat(actual6).isEqualTo(BigDecimal("4.5"))
    }

    @Test
    fun `get amount rate`() {
        val rates = mutableListOf<InterestRate>(
            InterestRate(1, null, BigDecimal("200000"), BigDecimal("1.6"), 1),
            InterestRate(2, null, BigDecimal("400000"), BigDecimal("2.6"), 1),
            InterestRate(3, null, BigDecimal("600000"), BigDecimal("3.6"), 1),
            InterestRate(4, null, BigDecimal("800000"), BigDecimal("4.6"), 1),
        )

        val actual1 = InterestRateHelper.getRate(BigDecimal(100000),LoanTermType.ONE_MONTH, rates)
        assertThat(actual1).isEqualTo(BigDecimal("1.6"))

        val actual2 = InterestRateHelper.getRate(BigDecimal(300000), LoanTermType.THREE_MONTHS, rates)
        assertThat(actual2).isEqualTo(BigDecimal("2.6"))

        val actual3 = InterestRateHelper.getRate(BigDecimal(500000), LoanTermType.SIX_MONTHS, rates)
        assertThat(actual3).isEqualTo(BigDecimal("3.6"))

        val actual4 = InterestRateHelper.getRate(BigDecimal(700000), LoanTermType.THREE_YEAR, rates)
        assertThat(actual4).isEqualTo(BigDecimal("4.6"))
    }
}