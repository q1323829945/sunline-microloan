package cn.sunline.saas.formula

import cn.sunline.saas.global.constant.BaseYearDays
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * @title: InterestRateUtil
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/21 20:19
 */
class InterestRateTest {

    private val interestRate: CalculateInterestRate = CalculateInterestRate(BigDecimal("7.2"))

    @Test
    fun `to day rate is calculated by account year`() {
        val actual = interestRate.toDayRate(BaseYearDays.ACCOUNT_YEAR)
        assertThat(actual).isEqualTo(BigDecimal("0.000200"))
    }

    @Test
    fun `to day rate is calculated by actual year`() {
        val actual = interestRate.toDayRate(BaseYearDays.ACTUAL_YEAR)
        assertThat(actual).isEqualTo(BigDecimal("0.000197"))
    }

    @Test
    fun `to month rate`() {
        val actual = interestRate.toMonthRate()
        assertThat(actual).isEqualTo(BigDecimal("0.006000"))
    }

    @Test
    fun `calculate overdue interest rate`() {
        val actual = interestRate.calOverdueInterestRate(BigDecimal("150"))
        assertThat(actual).isEqualTo(BigDecimal("0.108000"))
    }

    @Test
    fun `calculate rate when positive number`() {
        val actual = interestRate.calRate(BigDecimal("30"))
        assertThat(actual).isEqualTo(BigDecimal("0.093600"))
    }

    @Test
    fun `calculate rate when negative number`() {
        val actual = interestRate.calRate(BigDecimal("-30"))
        assertThat(actual).isEqualTo(BigDecimal("0.050400"))
    }

}