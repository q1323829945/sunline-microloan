package cn.sunline.saas.schedule

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.impl.PayInterestSchedulePrincipalMaturitySchedule
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.joda.time.Instant
import org.junit.jupiter.api.Test
import java.math.BigDecimal


class PayInterestSchedulePrincipalMaturityScheduleTest {

    @Test
    fun `test pay interest schedule principal maturity schedules`() {

        val actual = PayInterestSchedulePrincipalMaturitySchedule(
            BigDecimal("1000000"), BigDecimal("6.3"), LoanTermType.THREE_MONTHS,
            RepaymentFrequency.ONE_MONTH, DateTime.now(),null, BaseYearDays.ACCOUNT_YEAR).getSchedules()

        Assertions.assertThat(actual[0].instalment).isEqualTo(BigDecimal("16100.00"))
        Assertions.assertThat(actual[0].interest).isEqualTo(BigDecimal("16100.00"))
        Assertions.assertThat(actual[0].principal).isEqualTo(BigDecimal("0.00"))
        Assertions.assertThat(actual[0].remainingPrincipal).isEqualTo(BigDecimal("1000000.00"))

        Assertions.assertThat(actual[1].instalment).isEqualTo(BigDecimal("16100.00"))
        Assertions.assertThat(actual[1].interest).isEqualTo(BigDecimal("16100.00"))
        Assertions.assertThat(actual[1].principal).isEqualTo(BigDecimal("0.00"))
        Assertions.assertThat(actual[1].remainingPrincipal).isEqualTo(BigDecimal("1000000.00"))

        Assertions.assertThat(actual[2].instalment).isEqualTo(BigDecimal("1016100.00"))
        Assertions.assertThat(actual[2].interest).isEqualTo(BigDecimal("16100.00"))
        Assertions.assertThat(actual[2].principal).isEqualTo(BigDecimal("1000000.00"))
        Assertions.assertThat(actual[2].remainingPrincipal).isEqualTo(BigDecimal("0.00"))
    }
}