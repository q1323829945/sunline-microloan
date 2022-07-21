package cn.sunline.saas.schedule

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import cn.sunline.saas.schedule.impl.EqualInstalmentSchedule
import cn.sunline.saas.schedule.impl.EqualInstalmentSchedulePrepayment
import cn.sunline.saas.schedule.impl.EqualInstalmentScheduleReset
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.junit.jupiter.api.Disabled
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
            RepaymentFrequency.ONE_MONTH, RepaymentDayType.BASE_LOAN_DAY,BaseYearDays.ACCOUNT_YEAR,DateTime.now(),null,null,BigDecimal.ZERO).getSchedules()


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
    fun `test equal instalment schedules with base fisrt month`() {

        val actual = EqualInstalmentSchedule(BigDecimal("12529"), BigDecimal("2"), LoanTermType.THREE_MONTHS,
            RepaymentFrequency.ONE_MONTH, RepaymentDayType.MONTH_FIRST_DAY,BaseYearDays.ACCOUNT_YEAR,DateTime.now(),null,null,BigDecimal.ZERO).getSchedules()

        Assertions.assertThat(actual[0].instalment).isEqualTo(BigDecimal("4190.26"))
        Assertions.assertThat(actual[0].interest).isEqualTo(BigDecimal("23.74"))
        Assertions.assertThat(actual[0].principal).isEqualTo(BigDecimal("4166.52"))
        Assertions.assertThat(actual[0].remainingPrincipal).isEqualTo(BigDecimal("4182.04"))

        Assertions.assertThat(actual[1].interest).isEqualTo(BigDecimal("6.97"))
        Assertions.assertThat(actual[1].principal).isEqualTo(BigDecimal("4183.29"))

        Assertions.assertThat(actual[2].interest).isEqualTo(BigDecimal("11.07"))
        Assertions.assertThat(actual[2].principal).isEqualTo(BigDecimal("4179.19"))

    }



    @Test
    fun `test equal instalment schedules reset`() {


        val repaymentDateTime = DateTime(2022,6,28,0,0,0,0)
        val fromDateTime = DateTime(2022,6,28,0,0,0,0)
        val toDateTime = DateTime(2023,2,28,0,0,0,0)


        val actual = EqualInstalmentScheduleReset(BigDecimal("10000"), BigDecimal("12"), LoanTermType.ONE_YEAR,
            RepaymentFrequency.ONE_MONTH,RepaymentDayType.BASE_LOAN_DAY,BaseYearDays.ACCOUNT_YEAR,fromDateTime,toDateTime,repaymentDateTime,BigDecimal.ZERO).getSchedules()

        //TODO 补充单元
        Assertions.assertThat(actual[0].instalment).isEqualTo(BigDecimal("1350.00"))
        Assertions.assertThat(actual[0].interest).isEqualTo(BigDecimal("100.00"))
        Assertions.assertThat(actual[0].principal).isEqualTo(BigDecimal("1250.00"))

        Assertions.assertThat(actual[1].interest).isEqualTo(BigDecimal("87.50"))
        Assertions.assertThat(actual[1].principal).isEqualTo(BigDecimal("1250.00"))

        Assertions.assertThat(actual[2].interest).isEqualTo(BigDecimal("75.00"))
        Assertions.assertThat(actual[2].principal).isEqualTo(BigDecimal("1250.00"))

    }


    @Test
    fun `test equal instalment schedules prepayment`() {


        val repaymentDateTime = DateTime(2022,7,28,0,0,0,0)
        val fromDateTime = DateTime(2022,6,28,0,0,0,0)
        val toDateTime = DateTime(2023,9,28,0,0,0,0)


        val actual = EqualInstalmentSchedulePrepayment(BigDecimal("10000"), BigDecimal("12"), LoanTermType.THREE_MONTHS,
            RepaymentFrequency.ONE_MONTH,RepaymentDayType.BASE_LOAN_DAY,BaseYearDays.ACCOUNT_YEAR,fromDateTime,toDateTime,repaymentDateTime,BigDecimal.ZERO).getSchedules()

        //TODO 补充单元
        Assertions.assertThat(actual[0].instalment).isEqualTo(BigDecimal("3433.12"))
        Assertions.assertThat(actual[0].interest).isEqualTo(BigDecimal("99.90"))
        Assertions.assertThat(actual[0].principal).isEqualTo(BigDecimal("3333.22"))

        Assertions.assertThat(actual[1].interest).isEqualTo(BigDecimal("0.00"))
        Assertions.assertThat(actual[1].principal).isEqualTo(BigDecimal("3366.55"))

        Assertions.assertThat(actual[2].interest).isEqualTo(BigDecimal("0.00"))
        Assertions.assertThat(actual[2].principal).isEqualTo(BigDecimal("3400.22"))

    }
}