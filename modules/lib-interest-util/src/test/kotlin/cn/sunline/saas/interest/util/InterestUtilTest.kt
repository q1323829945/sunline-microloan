package cn.sunline.saas.interest.util

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
class InterestUtilTest {

    @Test
    fun `calculate month interest`() {
        val actual = InterestUtil.calMonthInterest(BigDecimal("10000000"), BigDecimal("0.600000"))
        Assertions.assertThat(actual).isEqualTo(BigDecimal("60000.00"))
    }

    @Test
    fun `calculate year interest`() {
        val actual = InterestUtil.calYearInterest(BigDecimal("10000000"), BigDecimal("7.2"))
        Assertions.assertThat(actual).isEqualTo(BigDecimal("720000.00"))
    }

    @Test
    fun `calculate days interest`() {
        val actual = InterestUtil.calDaysInterest(
            BigDecimal("10000000"),
            BigDecimal("0.02"),
            DateTime.now().toInstant(),
            DateTime.now().plusDays(3).toInstant()
        )
        Assertions.assertThat(actual).isEqualTo(BigDecimal("6000.00"))
    }
}