package cn.sunline.saas.channel.interest.component

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.channel.interest.model.InterestRate
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
    fun `get rate`() {
        val rates = mutableListOf<InterestRate>(
            InterestRate(1, LoanTermType.ONE_MONTH, BigDecimal("1.6"), 1),
            InterestRate(2, LoanTermType.THREE_MONTHS, BigDecimal("1.8"), 1),
            InterestRate(3, LoanTermType.SIX_MONTHS, BigDecimal("2.1"), 1),
            InterestRate(4, LoanTermType.THREE_YEAR, BigDecimal("2.5"), 1)
        )

        val actual1 = InterestRateHelper.getRate(LoanTermType.ONE_MONTH, rates)
        assertThat(actual1).isEqualTo(BigDecimal("1.6"))

        val actual2 = InterestRateHelper.getRate(LoanTermType.THREE_MONTHS, rates)
        assertThat(actual2).isEqualTo(BigDecimal("1.8"))

        val actual3 = InterestRateHelper.getRate(LoanTermType.ONE_YEAR, rates)
        assertThat(actual3).isEqualTo(BigDecimal("2.5"))
    }
}