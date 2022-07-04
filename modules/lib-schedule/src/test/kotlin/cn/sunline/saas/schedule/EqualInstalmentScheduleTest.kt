package cn.sunline.saas.schedule

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.impl.EqualInstalmentSchedule
import cn.sunline.saas.schedule.impl.EqualInstalmentScheduleReset
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
            RepaymentFrequency.ONE_MONTH, RepaymentDayType.BASE_LOAN_DAY,BaseYearDays.ACCOUNT_YEAR,DateTime.now(),null,null).getSchedules()


        Assertions.assertThat(actual[0].instalment).isEqualTo(BigDecimal("336839.44"))
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


    @Test
    fun `test equal instalment schedules reset`() {


        val repaymentDateTime = DateTime(2022,6,16,0,0,0,0)
        val fromDateTime = DateTime(2022,6,28,0,0,0,0)
        val toDateTime = DateTime(2023,2,28,0,0,0,0)


        val actual = EqualInstalmentScheduleReset(BigDecimal("10000"), BigDecimal("12"), LoanTermType.ONE_YEAR,
            RepaymentFrequency.ONE_MONTH,RepaymentDayType.BASE_LOAN_DAY,BaseYearDays.ACCOUNT_YEAR,DateTime.now(),toDateTime,repaymentDateTime).getSchedules()

        //TODO 补充单元
        Assertions.assertThat(actual[0].instalment).isEqualTo(BigDecimal("336839.44"))
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