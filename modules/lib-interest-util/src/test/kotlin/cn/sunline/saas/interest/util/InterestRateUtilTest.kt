package cn.sunline.saas.interest.util

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.constant.BaseYearDays
import cn.sunline.saas.interest.model.InterestRate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * @title: InterestRateUtil
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/21 20:19
 */
class InterestRateUtilTest {

    @Test
    fun `to day rate is calculated by account year`() {
        val actual = InterestRateUtil.toDayRate(BaseYearDays.ACCOUNT_YEAR, BigDecimal("7.2"))
        assertThat(actual).isEqualTo(BigDecimal("0.020000"))
    }

    @Test
    fun `to day rate is calculated by actual year`() {
        val actual = InterestRateUtil.toDayRate(BaseYearDays.ACTUAL_YEAR, BigDecimal("7.2"))
        assertThat(actual).isEqualTo(BigDecimal("0.019726"))
    }

    @Test
    fun `to month rate`() {
        val actual = InterestRateUtil.toMonthRate(BigDecimal("7.2"))
        assertThat(actual).isEqualTo(BigDecimal("0.600000"))
    }

    @Test
    fun `calculate overdue interest rate`() {
        val actual = InterestRateUtil.calOverdueInterestRate(BigDecimal("7.2"), BigDecimal("150"))
        assertThat(actual).isEqualTo(BigDecimal("10.800000"))
    }

    @Test
    fun `calculate rate when positive number`() {
        val actual = InterestRateUtil.calRate(BigDecimal("7.2"), BigDecimal("30"))
        assertThat(actual).isEqualTo(BigDecimal("9.360000"))
    }

    @Test
    fun `calculate rate when negative number`() {
        val actual = InterestRateUtil.calRate(BigDecimal("7.2"), BigDecimal("-30"))
        assertThat(actual).isEqualTo(BigDecimal("5.040000"))
    }

    @Test
    fun `get rate`() {
        val rates = mutableListOf<InterestRate>(
            InterestRate(1, LoanTermType.ONE_MONTH, BigDecimal("1.6"), 1),
            InterestRate(2, LoanTermType.THREE_MONTHS, BigDecimal("1.8"), 1),
            InterestRate(3, LoanTermType.SIX_MONTHS, BigDecimal("2.1"), 1),
            InterestRate(4, LoanTermType.THREE_YEAR, BigDecimal("2.5"), 1)
        )

        val actual1 = InterestRateUtil.getRate(LoanTermType.ONE_MONTH, rates)
        assertThat(actual1).isEqualTo(BigDecimal("1.6"))

        val actual2 = InterestRateUtil.getRate(LoanTermType.THREE_MONTHS, rates)
        assertThat(actual2).isEqualTo(BigDecimal("1.8"))

        val actual3 = InterestRateUtil.getRate(LoanTermType.ONE_YEAR, rates)
        assertThat(actual3).isEqualTo(BigDecimal("2.5"))
    }
}