package cn.sunline.saas.interest.arrangement.component

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.arrangement.model.db.InterestArrangement
import cn.sunline.saas.interest.constant.BaseYearDays
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.InterestRate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * @title: InterestArrangementComponentTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/23 20:06
 */
class InterestArrangementComponentTest {

    @Test
    fun `getExecutionRate when interestType is fixed`() {
        val interestArrangement = InterestArrangement(
            id = 1,
            interestType = InterestType.FIXED,
            rate = BigDecimal("7.5"),
            baseYearDays = BaseYearDays.ACTUAL_YEAR,
            adjustFrequency = "1",
            overdueInterestRatePercentage = BigDecimal("150")
        )
        val baseRates = mutableListOf(
            InterestRate(1, LoanTermType.ONE_MONTH, BigDecimal("1.6"), 1),
            InterestRate(2, LoanTermType.THREE_MONTHS, BigDecimal("1.8"), 1),
            InterestRate(3, LoanTermType.SIX_MONTHS, BigDecimal("2.1"), 1),
            InterestRate(4, LoanTermType.THREE_YEAR, BigDecimal("2.5"), 1)
        )
        val actual = interestArrangement.getExecutionRate(LoanTermType.ONE_YEAR,baseRates)

        assertThat(actual).isEqualTo(BigDecimal("7.5"))
    }

    @Test
    fun `getExecutionRate when interestType is floating`() {
        val interestArrangement = InterestArrangement(
            id = 1,
            interestType = InterestType.FLOATING_RATE_NOTE,
            rate = BigDecimal("30"),
            baseYearDays = BaseYearDays.ACTUAL_YEAR,
            adjustFrequency = "1",
            overdueInterestRatePercentage = BigDecimal("150")
        )
        val baseRates = mutableListOf(
            InterestRate(1, LoanTermType.ONE_MONTH, BigDecimal("1.6"), 1),
            InterestRate(2, LoanTermType.THREE_MONTHS, BigDecimal("1.8"), 1),
            InterestRate(3, LoanTermType.SIX_MONTHS, BigDecimal("2.1"), 1),
            InterestRate(4, LoanTermType.THREE_YEAR, BigDecimal("2.5"), 1)
        )
        val actual = interestArrangement.getExecutionRate(LoanTermType.ONE_YEAR,baseRates)

        assertThat(actual).isEqualTo(BigDecimal("3.250000"))
    }
}