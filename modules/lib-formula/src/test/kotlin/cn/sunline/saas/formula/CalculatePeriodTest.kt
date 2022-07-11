package cn.sunline.saas.formula

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.junit.jupiter.api.Disabled
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

        val startDateTime = DateTime.now()
        val actual = CalculatePeriod.getPeriodDatesByStandard(
            startDateTime,
            LoanTermType.SIX_MONTHS.term.calDate(startDateTime),
            RepaymentFrequency.ONE_MONTH
        )

        Assertions.assertThat(actual.size).isEqualTo(6)

    }

    @Test
    fun `test get period dates with lending date when minutes of toDateTime is less then minutes of fromDateTime `() {

        val startDateTime = DateTime.now()
        val actual = CalculatePeriod.getPeriodDatesByStandard(
            startDateTime,
            LoanTermType.SIX_MONTHS.term.calDate(startDateTime).plus(2),
            RepaymentFrequency.ONE_MONTH
        )

        Assertions.assertThat(actual.size).isEqualTo(6)

    }


    @Test
    fun `test get period dates ONE_MONTH with custom date MONTH_LAST_DAY`() {
        val startDateTime = DateTime(2022, 2, 28, 0, 0, 0, 0)
        val actual = CalculatePeriod.getPeriodDates(
            startDateTime,
            LoanTermType.ONE_YEAR.term.calDate(startDateTime),
            RepaymentFrequency.ONE_MONTH, RepaymentDayType.MONTH_LAST_DAY
        )
        Assertions.assertThat(actual.size).isEqualTo(12)
        Assertions.assertThat(actual.filter { it.isEnough }.size).isEqualTo(11)
        Assertions.assertThat(actual.filter { !it.isEnough }.size).isEqualTo(1)
    }

    @Test
    fun `test get the frequency ONE_MONTH period dates with custom repayment date MONTH_FIRST_DAY`() {
        val startDateTime = DateTime(2022, 2, 28, 0, 0, 0, 0)
        val actual = CalculatePeriod.getPeriodDates(
            startDateTime,
            LoanTermType.ONE_YEAR.term.calDate(startDateTime),
            RepaymentFrequency.ONE_MONTH, RepaymentDayType.MONTH_FIRST_DAY
        )
        Assertions.assertThat(actual.size).isEqualTo(13)
        Assertions.assertThat(actual.filter { it.isEnough }.size).isEqualTo(11)
        Assertions.assertThat(actual.filter { !it.isEnough }.size).isEqualTo(2)

    }

    @Test
    fun `test get repayment date when repayment day greater than 28 on february 28th`() {
        val fromDateTime = DateTime(2022, 2, 28, 0, 0, 0, 0)
        val toDateTime = DateTime(2022, 3, 28, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.adjustRepaymentDateTime(fromDateTime, toDateTime, 30)
        val expectedDate = DateTime(2022, 3, 30, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)

    }

    @Test
    fun `test get repayment date when repayment day less than 28 on february 28th`() {
        val fromDateTime = DateTime(2022, 2, 28, 0, 0, 0, 0)
        val toDateTime = DateTime(2022, 3, 28, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.adjustRepaymentDateTime(fromDateTime, toDateTime, 22)
        val expectedDate = DateTime(2022, 3, 22, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }


    @Test
    fun `test get repayment date when repayment day equals 28 on february 28th`() {
        val fromDateTime = DateTime(2022, 2, 28, 0, 0, 0, 0)
        val toDateTime = DateTime(2022, 3, 28, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.adjustRepaymentDateTime(fromDateTime, toDateTime, 28)
        val expectedDate = DateTime(2022, 3, 28, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date when repayment day greater than 29 on february 29th`() {
        val fromDateTime = DateTime(2024, 2, 29, 0, 0, 0, 0)
        val toDateTime = DateTime(2024, 3, 29, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.adjustRepaymentDateTime(fromDateTime, toDateTime, 30)
        val expectedDate = DateTime(2024, 3, 30, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date when repayment day less than 29 on february 29th`() {
        val fromDateTime = DateTime(2024, 2, 29, 0, 0, 0, 0)
        val toDateTime = DateTime(2024, 3, 29, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.adjustRepaymentDateTime(fromDateTime, toDateTime, 22)
        val expectedDate = DateTime(2024, 3, 22, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date when repayment day equals 29 on february 29th`() {
        val fromDateTime = DateTime(2024, 2, 29, 0, 0, 0, 0)
        val toDateTime = DateTime(2024, 3, 29, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.adjustRepaymentDateTime(fromDateTime, toDateTime, 29)
        val expectedDate = DateTime(2024, 3, 29, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date when repayment day equals 31 on January 31st`() {
        val fromDateTime = DateTime(2022, 1, 31, 0, 0, 0, 0)
        val toDateTime = DateTime(2022, 2, 28, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.adjustRepaymentDateTime(fromDateTime, toDateTime, 31)
        val expectedDate = DateTime(2022, 2, 28, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date when repayment day less than 31 on January 31st`() {
        val fromDateTime = DateTime(2022, 1, 31, 0, 0, 0, 0)
        val toDateTime = DateTime(2022, 2, 28, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.adjustRepaymentDateTime(fromDateTime, toDateTime, 30)
        val expectedDate = DateTime(2022, 2, 28, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date when repayment day greater than 25 on January 25th`() {
        val fromDateTime = DateTime(2022, 1, 25, 0, 0, 0, 0)
        val toDateTime = DateTime(2022, 2, 25, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.adjustRepaymentDateTime(fromDateTime, toDateTime, 27)
        val expectedDate = DateTime(2022, 1, 27, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date when repayment day equals 30 on April 30th`() {
        val fromDateTime = DateTime(2022, 4, 30, 0, 0, 0, 0)
        val toDateTime = DateTime(2022, 5, 30, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.adjustRepaymentDateTime(fromDateTime, toDateTime, 30)
        val expectedDate = DateTime(2022, 5, 30, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date when repayment day less than 30 on April 30th`() {
        val fromDateTime = DateTime(2022, 4, 30, 0, 0, 0, 0)
        val toDateTime = DateTime(2022, 5, 30, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.adjustRepaymentDateTime(fromDateTime, toDateTime, 20)
        val expectedDate = DateTime(2022, 5, 20, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date when repayment day greater than 25 on April 25th`() {
        val fromDateTime = DateTime(2022, 4, 25, 0, 0, 0, 0)
        val toDateTime = DateTime(2022, 5, 25, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.adjustRepaymentDateTime(fromDateTime, toDateTime, 27)
        val expectedDate = DateTime(2022, 4, 27, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date April2 25 and greater than 25`() {

        val startDateTime = DateTime(2022, 3, 1, 0, 0, 0, 0)
        val toDateTime = DateTime(2022, 4, 5, 0, 0, 0, 0)

        val repaymentDate =
            CalculatePeriod.getPeriodDatesByCustom(startDateTime, toDateTime, RepaymentFrequency.ONE_MONTH, 1)
        val expectedDate = DateTime(2022, 4, 27, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }
}