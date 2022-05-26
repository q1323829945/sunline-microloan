package cn.sunline.saas.schedule

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.impl.EqualInstalmentSchedule
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * @title: CalculateEqualInstalmentTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/20 11:34
 */
class EqualInstalmentScheduleTest {

    @Test
    fun `test equal instalment schedules`() {

        val actual = EqualInstalmentSchedule(BigDecimal("1000000"), BigDecimal("6.3"), LoanTermType.THREE_MONTHS,
            RepaymentFrequency.ONE_MONTH, DateTime.now(),null).getSchedules()


        Assertions.assertThat(actual[0].installment).isEqualTo(BigDecimal("336839.44"))
        Assertions.assertThat(actual[0].interest).isEqualTo(BigDecimal("5250.00"))
        Assertions.assertThat(actual[0].principal).isEqualTo(BigDecimal("331589.44"))
        Assertions.assertThat(actual[0].remainingPrincipal).isEqualTo(BigDecimal("668410.56"))

        Assertions.assertThat(actual[1].interest).isEqualTo(BigDecimal("3509.16"))
        Assertions.assertThat(actual[1].principal).isEqualTo(BigDecimal("333330.28"))
        Assertions.assertThat(actual[1].remainingPrincipal).isEqualTo(BigDecimal("335080.28"))

        Assertions.assertThat(actual[2].interest).isEqualTo(BigDecimal("1759.16"))
        Assertions.assertThat(actual[2].principal).isEqualTo(BigDecimal("335080.28"))
        Assertions.assertThat(actual[2].remainingPrincipal).isEqualTo(BigDecimal("0.00"))

    }
}