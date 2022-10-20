package cn.sunline.saas.formula

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.CalculatePrecision
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.joda.time.Days
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @title: InterestUtilTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/23 11:38
 */
class CalculateInterestTest {

    private val calculateInterest = CalculateInterest(BigDecimal("10000000"), CalculateInterestRate(BigDecimal("7.2")))

    @Test
    fun `calculate month interest`() {
        val actual = calculateInterest.getMonthInterest()
        Assertions.assertThat(actual).isEqualTo(BigDecimal("60000.000000"))
    }

    @Test
    fun `calculate year interest`() {
        val actual = calculateInterest.getYearInterest()
        Assertions.assertThat(actual).isEqualTo(BigDecimal("720000.000000"))
    }

    @Test
    fun `calculate days interest`() {
        val actual = calculateInterest.getDaysInterest(
            DateTime.now(),
            DateTime.now().plusDays(3), BaseYearDays.ACCOUNT_YEAR
        )

        Assertions.assertThat(actual).isEqualTo(BigDecimal("6000.000000"))
    }

    @Test
    fun `calculate day interest`() {
        val actual = calculateInterest.getDayInterest(
            12,
            BaseYearDays.ACCOUNT_YEAR
        )

        Assertions.assertThat(actual).isEqualTo(BigDecimal("24000.000000"))
    }

    @Test
    fun `calculate float interest`() {
        val rate = CalculateInterestRate(
            yearInterestRatePercent = BigDecimal(3.55)
        ).calRateWithNoPercent(
            floatPoint = BigDecimal(0.55),
            floatRatio = BigDecimal(66)
        )
        Assertions.assertThat(rate).isEqualTo(BigDecimal("0.632430"))
    }

    @Test
    fun `calculate fixed interest`() {
        val rate = CalculateInterestRate(
            yearInterestRatePercent = BigDecimal(3.55)
        ).calRateWithNoPercent(
            floatPoint = BigDecimal(0),
            floatRatio = BigDecimal(0)
        )
        Assertions.assertThat(rate).isEqualTo(BigDecimal("0.035500"))
    }

}