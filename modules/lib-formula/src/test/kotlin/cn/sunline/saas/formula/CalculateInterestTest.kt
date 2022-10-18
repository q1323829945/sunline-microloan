package cn.sunline.saas.formula

import cn.sunline.saas.global.constant.BaseYearDays
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * @title: InterestUtilTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/23 11:38
 */
class CalculateInterestTest {

    private val calculateInterest = CalculateInterest(BigDecimal("10000000"),CalculateInterestRate(BigDecimal("7.2")))

    @Test
    fun `calculate month interest`() {
        val actual = calculateInterest.getMonthInterest()
        Assertions.assertThat(actual).isEqualTo(BigDecimal("60000.000000"))
    }

    @Test
    fun `calculate float interest`() {
        val rate = CalculateInterestRate(BigDecimal(1.5)).calRateWithNoPercent(BigDecimal(3), BigDecimal(0.66), BigDecimal(0.66))
        Assertions.assertThat(rate).isEqualTo(BigDecimal("4.509999"))
    }

    @Test
    fun `calculate year interest`() {
        val actual = calculateInterest.getYearInterest()
        Assertions.assertThat(actual).isEqualTo(BigDecimal("720000.000000"))
    }

    @Test
    fun `calculate days interest`() {
        val actual = calculateInterest.getDaysInterest(DateTime.now(),
            DateTime.now().plusDays(3),BaseYearDays.ACCOUNT_YEAR)

        Assertions.assertThat(actual).isEqualTo(BigDecimal("6000.000000"))
    }

}