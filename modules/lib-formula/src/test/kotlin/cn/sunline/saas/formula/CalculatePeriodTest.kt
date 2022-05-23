package cn.sunline.saas.formula

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentFrequency
import org.assertj.core.api.Assertions
import org.joda.time.Instant
import org.junit.jupiter.api.Test

/**
 * @title: CalculatePeriodTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/19 11:49
 */
class CalculatePeriodTest {

    @Test
    fun `test term less than frequency`() {
        val actual = CalculatePeriod.calculatePeriods(LoanTermType.SIX_MONTHS, RepaymentFrequency.ONE_YEAR)
        Assertions.assertThat(actual).isEqualTo(1)
    }

    @Test
    fun `test term equal to frequency multiple n`() {
        val actual1 = CalculatePeriod.calculatePeriods(LoanTermType.SIX_MONTHS, RepaymentFrequency.ONE_MONTH)
        Assertions.assertThat(actual1).isEqualTo(6)

        val actual2 = CalculatePeriod.calculatePeriods(LoanTermType.SIX_MONTHS, RepaymentFrequency.SIX_MONTHS)
        Assertions.assertThat(actual2).isEqualTo(1)
    }

    @Test
    fun `test get period dates with lending date`() {

        val startDateTime = Instant.now()
        val actual = CalculatePeriod.getPeriodDatesByStandard(
            startDateTime,
            LoanTermType.SIX_MONTHS.term.calDate(startDateTime),
            RepaymentFrequency.ONE_MONTH
        )

        Assertions.assertThat(actual.size).isEqualTo(6)

    }

    @Test
    fun `test get period dates with lending date when minutes of toDateTime is less then minutes of fromDateTime `() {

        val startDateTime = Instant.now()
        val actual = CalculatePeriod.getPeriodDatesByStandard(
            startDateTime,
            LoanTermType.SIX_MONTHS.term.calDate(startDateTime).plus(2),
            RepaymentFrequency.ONE_MONTH
        )

        Assertions.assertThat(actual.size).isEqualTo(6)

    }

}