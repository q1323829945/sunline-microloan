package cn.sunline.saas.schedule

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.impl.EqualPrincipalSchedule
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.junit.jupiter.api.Disabled
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
            RepaymentFrequency.THREE_MONTHS, RepaymentDayType.BASE_LOAN_DAY,
            BaseYearDays.ACCOUNT_YEAR, DateTime.now(), null, null,BigDecimal.ZERO
        ).getSchedules()

        Assertions.assertThat(actual[0].instalment).isEqualTo(BigDecimal("1015750.00"))
        Assertions.assertThat(actual[0].interest).isEqualTo(BigDecimal("15750.00"))
        Assertions.assertThat(actual[0].principal).isEqualTo(BigDecimal("1000000.00"))
        Assertions.assertThat(actual[0].remainingPrincipal).isEqualTo(BigDecimal("0.00"))
    }

    @Test
    fun `test equal Principal schedules1`() {

        val actual = EqualPrincipalSchedule(
            BigDecimal("10000"), BigDecimal("12"), LoanTermType.THREE_MONTHS,
            RepaymentFrequency.ONE_MONTH, RepaymentDayType.MONTH_FIRST_DAY,
            BaseYearDays.ACCOUNT_YEAR, DateTime.now(), null, null,BigDecimal.ZERO
        ).getSchedules()

        Assertions.assertThat(actual[0].instalment).isEqualTo(BigDecimal("3448.87"))
        Assertions.assertThat(actual[0].interest).isEqualTo(BigDecimal("115.54"))
        Assertions.assertThat(actual[0].principal).isEqualTo(BigDecimal("3333.33"))
        Assertions.assertThat(actual[0].remainingPrincipal).isEqualTo(BigDecimal("6666.67"))

        Assertions.assertThat(actual[1].instalment).isEqualTo(BigDecimal("3400.00"))
        Assertions.assertThat(actual[1].interest).isEqualTo(BigDecimal("66.67"))
        Assertions.assertThat(actual[1].principal).isEqualTo(BigDecimal("3333.33"))
        Assertions.assertThat(actual[1].remainingPrincipal).isEqualTo(BigDecimal("3333.34"))

        Assertions.assertThat(actual[2].instalment).isEqualTo(BigDecimal("3352.21"))
        Assertions.assertThat(actual[2].interest).isEqualTo(BigDecimal("18.87"))
        Assertions.assertThat(actual[2].principal).isEqualTo(BigDecimal("3333.34"))
        Assertions.assertThat(actual[2].remainingPrincipal).isEqualTo(BigDecimal("0.00"))
    }
}