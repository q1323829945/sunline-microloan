package cn.sunline.saas.schedule

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.impl.OneOffRepaymentSchedule
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.joda.time.Instant
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.math.BigDecimal


class OneOffRepaymentScheduleTest {

    @Test
    fun `test One Off Repayment schedules`() {

        val actual = OneOffRepaymentSchedule(
            BigDecimal("1000000"), BigDecimal("6.3"), LoanTermType.ONE_YEAR,
            RepaymentFrequency.ONE_MONTH, RepaymentDayType.BASE_LOAN_DAY,BaseYearDays.ACCOUNT_YEAR,DateTime.now(),null,null,BigDecimal.ZERO).getSchedules()

        Assertions.assertThat(actual[0].instalment).isEqualTo(BigDecimal("1063875.00"))
        Assertions.assertThat(actual[0].interest).isEqualTo(BigDecimal("63875.00"))
        Assertions.assertThat(actual[0].principal).isEqualTo(BigDecimal("1000000.00"))
        Assertions.assertThat(actual[0].remainingPrincipal).isEqualTo(BigDecimal("0.00"))
    }
}