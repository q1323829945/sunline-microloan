package cn.sunline.saas.schedule

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.impl.PayInterestSchedulePrincipalMaturitySchedule
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.joda.time.Instant
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.math.BigDecimal


class PayInterestSchedulePrincipalMaturityScheduleTest {

    @Test
    fun `test pay interest schedule principal maturity schedules`() {

        val actual = PayInterestSchedulePrincipalMaturitySchedule(
            BigDecimal("1000000"),
            BigDecimal("6.3"),
            LoanTermType.THREE_MONTHS,
            RepaymentFrequency.ONE_MONTH,
            RepaymentDayType.BASE_LOAN_DAY,
            BaseYearDays.ACCOUNT_YEAR,
            DateTime.now(),
            null,
            null,
            BigDecimal.ZERO
        ).getSchedules()

        Assertions.assertThat(actual[0].instalment).isEqualTo(BigDecimal("5425.00"))
        Assertions.assertThat(actual[0].interest).isEqualTo(BigDecimal("5425.00"))
        Assertions.assertThat(actual[0].principal).isEqualTo(BigDecimal("0.00"))
        Assertions.assertThat(actual[0].remainingPrincipal).isEqualTo(BigDecimal("1000000.00"))

        Assertions.assertThat(actual[1].instalment).isEqualTo(BigDecimal("5425.00"))
        Assertions.assertThat(actual[1].interest).isEqualTo(BigDecimal("5425.00"))
        Assertions.assertThat(actual[1].principal).isEqualTo(BigDecimal("0.00"))
        Assertions.assertThat(actual[1].remainingPrincipal).isEqualTo(BigDecimal("1000000.00"))

        Assertions.assertThat(actual[2].instalment).isEqualTo(BigDecimal("1005250.00"))
        Assertions.assertThat(actual[2].interest).isEqualTo(BigDecimal("5250.00"))
        Assertions.assertThat(actual[2].principal).isEqualTo(BigDecimal("1000000.00"))
        Assertions.assertThat(actual[2].remainingPrincipal).isEqualTo(BigDecimal("0.00"))
    }

    @Test
    @Disabled
    fun `test pay interest schedule principal maturity schedules1`() {

        val actual = PayInterestSchedulePrincipalMaturitySchedule(
            BigDecimal("10000"),
            BigDecimal("12"),
            LoanTermType.THREE_MONTHS,
            RepaymentFrequency.ONE_MONTH,
            RepaymentDayType.MONTH_FIRST_DAY,
            BaseYearDays.ACCOUNT_YEAR,
            DateTime.now(),
            null,
            null,
            BigDecimal.ZERO
        ).getSchedules()

        Assertions.assertThat(actual[0].instalment).isEqualTo(BigDecimal("149.85"))
        Assertions.assertThat(actual[0].interest).isEqualTo(BigDecimal("149.85"))
        Assertions.assertThat(actual[0].principal).isEqualTo(BigDecimal("0.00"))
        Assertions.assertThat(actual[0].remainingPrincipal).isEqualTo(BigDecimal("10000.00"))

        Assertions.assertThat(actual[1].instalment).isEqualTo(BigDecimal("99.90"))
        Assertions.assertThat(actual[1].interest).isEqualTo(BigDecimal("99.90"))
        Assertions.assertThat(actual[1].principal).isEqualTo(BigDecimal("0.00"))
        Assertions.assertThat(actual[1].remainingPrincipal).isEqualTo(BigDecimal("10000.00"))

        Assertions.assertThat(actual[2].instalment).isEqualTo(BigDecimal("10056.61"))
        Assertions.assertThat(actual[2].interest).isEqualTo(BigDecimal("56.61"))
        Assertions.assertThat(actual[2].principal).isEqualTo(BigDecimal("10000.00"))
        Assertions.assertThat(actual[2].remainingPrincipal).isEqualTo(BigDecimal("0.00"))
    }
}