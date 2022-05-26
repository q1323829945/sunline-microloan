package cn.sunline.saas.schedule

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.impl.EqualPrincipalSchedule
import org.assertj.core.api.Assertions
import org.joda.time.Instant
import org.junit.jupiter.api.Test
import java.math.BigDecimal


/**
 * @tittle :
 * @description :
 * @author : xujm
 * @date : 2022/5/25 13:48
 */
class EqualPrincipalScheduleTest {

    @Test
    fun `test equal Principal schedules`() {

        val actual = EqualPrincipalSchedule(
            BigDecimal("1000000"), BigDecimal("6.3"), LoanTermType.THREE_MONTHS,
            RepaymentFrequency.ONE_MONTH, Instant.now(),null).getSchedules()

        Assertions.assertThat(actual[0].instalment).isEqualTo(BigDecimal("338583.33"))
        Assertions.assertThat(actual[0].interest).isEqualTo(BigDecimal("5250.00"))
        Assertions.assertThat(actual[0].principal).isEqualTo(BigDecimal("333333.33"))
        Assertions.assertThat(actual[0].remainingPrincipal).isEqualTo(BigDecimal("666666.67"))

        Assertions.assertThat(actual[1].instalment).isEqualTo(BigDecimal("336833.33"))
        Assertions.assertThat(actual[1].interest).isEqualTo(BigDecimal("3500.00"))
        Assertions.assertThat(actual[1].principal).isEqualTo(BigDecimal("333333.33"))
        Assertions.assertThat(actual[1].remainingPrincipal).isEqualTo(BigDecimal("333333.34"))

        Assertions.assertThat(actual[2].instalment).isEqualTo(BigDecimal("335083.34"))
        Assertions.assertThat(actual[2].interest).isEqualTo(BigDecimal("1750.00"))
        Assertions.assertThat(actual[2].principal).isEqualTo(BigDecimal("333333.34"))
        Assertions.assertThat(actual[2].remainingPrincipal).isEqualTo(BigDecimal("0.00"))
    }
}