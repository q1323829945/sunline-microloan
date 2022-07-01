package cn.sunline.saas.formula

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.RepaymentDayType
import cn.sunline.saas.global.constant.RepaymentFrequency
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
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
        CalculatePeriod.getPeriodDates(
            startDateTime,
            LoanTermType.ONE_YEAR.term.calDate(startDateTime),
            RepaymentFrequency.ONE_MONTH, RepaymentDayType.MONTH_LAST_DAY,31)
    }

    @Test
    fun `test get period dates ONE_MONTH with custom date 1 MONTH_FIRST_DAY`() {

        val startDateTime = DateTime(2022, 2, 28, 0, 0, 0, 0)
        CalculatePeriod.getPeriodDates(
            startDateTime,
            LoanTermType.ONE_YEAR.term.calDate(startDateTime),
            RepaymentFrequency.ONE_MONTH, RepaymentDayType.MONTH_FIRST_DAY,1)
    }

    @Test
    fun `test get repayment date February 28 and greater than 28`() {

        val startDateTime = DateTime(2022, 2, 28, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.getRepaymentDateTime(startDateTime, 30)
        val expectedDate = DateTime(2022, 3, 30, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)

    }

    @Test
    fun `test get repayment date February 29 and greater than 29`() {

        val startDateTime = DateTime(2024, 2, 29, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.getRepaymentDateTime(startDateTime, 30)
        val expectedDate = DateTime(2024, 3, 30, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date February 28 and less than 28`() {

        val startDateTime = DateTime(2022, 2, 28, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.getRepaymentDateTime(startDateTime, 22)
        val expectedDate = DateTime(2022, 2, 22, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date February 29 and less than 29`() {

        val startDateTime = DateTime(2024, 2, 29, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.getRepaymentDateTime(startDateTime, 22)
        val expectedDate = DateTime(2024, 2, 22, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date February 28 and equals 28`() {

        val startDateTime = DateTime(2022, 2, 28, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.getRepaymentDateTime(startDateTime, 28)
        val expectedDate = DateTime(2022, 2, 28, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date February 29 and equals 29`() {

        val startDateTime = DateTime(2024, 2, 29, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.getRepaymentDateTime(startDateTime, 29)
        val expectedDate = DateTime(2024, 2, 29, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date January 31 and equals 31`() {

        val startDateTime = DateTime(2022, 1, 31, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.getRepaymentDateTime(startDateTime, 31)
        val expectedDate = DateTime(2022, 1, 31, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date January 31 and less than 31`() {

        val startDateTime = DateTime(2022, 1, 31, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.getRepaymentDateTime(startDateTime, 30)
        val expectedDate = DateTime(2022, 1, 30, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date January 25 and greater than 25`() {

        val startDateTime = DateTime(2022, 1, 25, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.getRepaymentDateTime(startDateTime, 27)
        val expectedDate = DateTime(2022, 1, 27, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date April 30 and equals 30`() {

        val startDateTime = DateTime(2022, 4, 30, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.getRepaymentDateTime(startDateTime, 30)
        val expectedDate = DateTime(2022, 4, 30, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date April 30 and less than 30`() {

        val startDateTime = DateTime(2022, 4, 30, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.getRepaymentDateTime(startDateTime, 20)
        val expectedDate = DateTime(2022, 4, 20, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }

    @Test
    fun `test get repayment date April 25 and greater than 25`() {

        val startDateTime = DateTime(2022, 4, 25, 0, 0, 0, 0)
        val repaymentDate = CalculatePeriod.getRepaymentDateTime(startDateTime, 27)
        val expectedDate = DateTime(2022, 4, 27, 0, 0, 0, 0)
        Assertions.assertThat(repaymentDate).isEqualTo(expectedDate)
    }
}